/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrjavaclient;

/**
 *
 * @author karol
 */
public class MessageProcessor {
    
    private Client client;
    private int clientID;
    
    public MessageProcessor(Client client) {
        this.client = client;
        this.clientID = client.getID();
    }
    
    public synchronized void processMessage(int messageID, Object message) {
        switch(messageID) {
            case Constants.REPLY : 
                MessageReply reply = (MessageReply) message;
                processMessage(reply);
                break;
        }
    }
    
    private void processMessage(MessageReply reply) {
        LogWriter.log(clientID, "Received REPLY:" + Constants.NEWLINE + reply.toString());
        client.stopTimeoutChecker();
        checkViewChange(reply);
    }
    
    public void sendMessage(MessageRequest request) {
        new Thread(new ClientRunnable(
                clientID,
                client.getPrimary().getIpAddress(),
                client.getPrimary().getPort(),
                1, 
                request)
        ).start();
        client.restartTimeoutChecker(request);
    }
    
    public void sendToAll(MessageRequest request) {
        for(ReplicaInfo info : client.getReplicaTable()) {
            new Thread(new ClientRunnable(
                    clientID,
                    info.getIpAddress(),
                    info.getPort(),
                    1, 
                    request)
            ).start();
        }
    }
    
    public void checkViewChange(MessageReply reply) {
        if(client.getViewNumber() != reply.getViewNumber()) {
            client.setViewNumber(reply.getViewNumber());
            int index = (reply.getViewNumber()-1) % client.getReplicaTable().size();
            client.setPrimary(client.getReplicaTable().get(index));
            LogWriter.log(clientID, "Primary changed.");
        }
    }
}
