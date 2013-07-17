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
    }
    
    public void sendMessage(MessageRequest request) {
        new Thread(new ClientRunnable(
                clientID,
                client.getPrimary().getIpAddress(),
                client.getPrimary().getPort(),
                1, 
                request)
        ).start();
    } 
}
