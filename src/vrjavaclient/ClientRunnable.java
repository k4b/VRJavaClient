/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrjavaclient;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author karol
 */
public class ClientRunnable implements Runnable{

    protected String serverAddress;
    protected int serverPort;
    protected int messageID;
    protected Object message;
    protected boolean isStopped;
    protected Thread runningThread= null;
    protected Socket clientSocket;
    
    public ClientRunnable(String serverAddress, int serverPort, int messageID, Object message) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.messageID = messageID;
        this.message = message;
    }
    
    @Override
    public void run() {
        System.out.println("Client started");
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        try {
            clientSocket = new Socket(serverAddress, serverPort);
            sendMessage();
        } catch (IOException e) {
            if(isStopped()) {
                System.out.println("Client communication crashed.") ;
                return;
            }
            throw new RuntimeException("Error accepting server connection", e);
        }
        System.out.println("Client communication Stopped.") ;
    }
    
    private synchronized boolean isStopped() {            
        return this.isStopped;
    }
    
    private void sendMessage() {
        if(messageID == 1) {
            MessageRequest request = (MessageRequest) message;
            System.out.println("Sending message:");
            System.out.println(request.toString());
            try {    
                DataOutputStream dataOutput = new DataOutputStream(clientSocket.getOutputStream());
                byte[] messageIDBytes = MyByteUtils.toByteArray(messageID);
                dataOutput.writeInt(messageIDBytes.length);
                dataOutput.write(messageIDBytes);
                
                System.out.println("sent: "+ MyByteUtils.byteArrayToInt(messageIDBytes));
                
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
                dataOutput.flush();
                
                //Colecting reply
                DataInputStream dataInput = new DataInputStream(clientSocket.getInputStream());
                int size = dataInput.readInt();
                byte[] replyIDBytes = new byte[size];
                dataInput.read(replyIDBytes, 0, size);
                size = dataInput.readInt();
                byte[] replyViewNumberBytes = new byte[size];
                dataInput.read(replyViewNumberBytes, 0, size);
                size = dataInput.readInt();
                byte[] replyRequestNumberBytes = new byte[size];
                dataInput.read(replyRequestNumberBytes, 0, size);
                size = dataInput.readInt();
                byte[] replyResultBytes = new byte[size];
                dataInput.read(replyResultBytes, 0, size);
                
                dataOutput.close();
                dataInput.close();
                clientSocket.close();
                
                MessageReply reply = new MessageReply(
                        MyByteUtils.byteArrayToInt(replyIDBytes),
                        MyByteUtils.byteArrayToInt(replyViewNumberBytes),
                        MyByteUtils.byteArrayToInt(replyRequestNumberBytes),
                        MyByteUtils.byteArrayToBoolean(replyResultBytes));
                
                System.out.println("Received reply:");
                System.out.println(reply.toString());
                
                
            } catch (IOException ex) {
                Logger.getLogger(ClientRunnable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }    
}
