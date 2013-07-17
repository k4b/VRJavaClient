/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrjavaclient;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author karol
 */
public class ServerRunnable implements Runnable{

    protected int clientID;
    protected Socket clientSocket = null;
    protected MessageProcessor messageProcessor;

    public ServerRunnable(int clientID, Socket clientSocket, MessageProcessor messageProcessor) {
        this.clientID = clientID;
        this.clientSocket = clientSocket;
        this.messageProcessor = messageProcessor;
    }

    @Override
    public void run() {
        try {            
//            LogWriter.log(clientID, "Receiving message...");
            DataInputStream dataInput = new DataInputStream(clientSocket.getInputStream());
            int size = dataInput.readInt();
            byte[] requestIDBytes = new byte[size];
            dataInput.read(requestIDBytes, 0, size);
            int messageID = MyByteUtils.byteArrayToInt(requestIDBytes);
            Object objectMessage = receiveMessage(messageID);
            
            messageProcessor.processMessage(messageID, objectMessage);
            dataInput.close();
            clientSocket.close();
            
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
    
    private synchronized Object receiveMessage(int messageID) {
        Object object = null;
        switch(messageID) {
            case Constants.REPLY :
                object = receiveReply();
                break;
        }
        return object;
    }
    
    private MessageReply receiveReply() {
        DataInputStream dataInput = null;
        MessageReply reply = null;
        try {
            dataInput = new DataInputStream(clientSocket.getInputStream());
            int size = dataInput.readInt();
            byte[] replyViewNumberBytes = new byte[size];
            dataInput.read(replyViewNumberBytes, 0, size);
            size = dataInput.readInt();
            byte[] replyRequestNumberBytes = new byte[size];
            dataInput.read(replyRequestNumberBytes, 0, size);
            size = dataInput.readInt();
            byte[] replyResultBytes = new byte[size];
            dataInput.read(replyResultBytes, 0, size);
            
            reply = new MessageReply(
                    MyByteUtils.byteArrayToInt(replyViewNumberBytes),
                    MyByteUtils.byteArrayToInt(replyRequestNumberBytes),
                    MyByteUtils.byteArrayToBoolean(replyResultBytes));
        } catch (IOException ex) {
            Logger.getLogger(ServerRunnable.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return reply;
        }
    }
}