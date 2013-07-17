/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrjavaclient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author karol
 */
public class ClientRunnable implements Runnable {

    private int clientID;
    private String serverAddress;
    private int serverPort;
    private int messageID;
    private Object message;
    private boolean isStopped;
    private Thread runningThread= null;
    private Socket clientSocket;
    
    public ClientRunnable(int clientID, String serverAddress, int serverPort, int messageID, Object message) {
        this.clientID = clientID;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.messageID = messageID;
        this.message = message;
    }
    
    @Override
    public void run() {
//        LogWriter.log( replica.getReplicaID(), "Client started");
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        try {
            clientSocket = new Socket(serverAddress, serverPort);
            send();
        } catch (IOException e) {
            if(isStopped()) {
                LogWriter.log( clientID, "Client communication crashed.") ;
                return;
            }
            throw new RuntimeException("Error accepting server connection", e);
        }
//        LogWriter.log( replica.getReplicaID(), "Client communication Stopped.") ;
    }
    
    private synchronized boolean isStopped() {            
        return this.isStopped;
    }
    
    private void send() {
        switch(messageID) {
            case Constants.REQUEST :
                MessageRequest request = (MessageRequest) message;
                send(request);
                break;
        }
    }
    
    private void send(MessageRequest request) {
        LogWriter.log(clientID, "Sending message REQUEST:" + Constants.NEWLINE + request.toString());
        DataOutputStream dataOutput = null;
        try {    
            dataOutput = new DataOutputStream(clientSocket.getOutputStream());
            byte[] messageIDBytes = MyByteUtils.toByteArray(messageID);
            dataOutput.writeInt(messageIDBytes.length);
            dataOutput.write(messageIDBytes);

            byte[] operationIDBytes = MyByteUtils.toByteArray(request.getOperation().getOperationID());
            dataOutput.writeInt(operationIDBytes.length);
            dataOutput.write(operationIDBytes);
            byte[] operationPathBytes = MyByteUtils.toByteArray(request.getOperation().getPath());
            dataOutput.writeInt(operationPathBytes.length);
            dataOutput.write(operationPathBytes);
            if(request.getOperation().getOperationID() == 1) {
                byte[] operationFile = request.getOperation().getFile();
                if(operationFile != null) {
                    dataOutput.writeInt(operationFile.length);
                    dataOutput.write(operationFile);
                } else {
                    dataOutput.writeInt(1);
                    byte[] nullFile = new byte[1];
                    nullFile[0] = 0;
                    dataOutput.write(nullFile);
                }
            }
            byte[] clientIDBytes = MyByteUtils.toByteArray(request.getClientID());
            dataOutput.writeInt(clientIDBytes.length);
            dataOutput.write(clientIDBytes);
            byte[] requestNumberBytes = MyByteUtils.toByteArray(request.getRequestNumber());
            dataOutput.writeInt(requestNumberBytes.length);
            dataOutput.write(requestNumberBytes);
            byte[] viewNumberBytes = MyByteUtils.toByteArray(request.getViewNumber());
            dataOutput.writeInt(viewNumberBytes.length);
            dataOutput.write(viewNumberBytes);
        } catch (IOException ex) {
            Logger.getLogger(ClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dataOutput.flush();
                dataOutput.close();
                clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }    
}
