/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrjavaclient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author karol
 */
public class VRProxy extends Thread{
    
    private int clientID;
    private boolean isStopped = false;
    private ServerSocket serverSocket = null;
    private int serverPort;
    private MessageProcessor messageProcessor;
    
    public VRProxy(int clientID, int serverPort, MessageProcessor messageProcessor) {
        super();
        this.clientID = clientID;
        this.serverPort = serverPort;
        this.messageProcessor = messageProcessor;
    }
    
    @Override
    public void run() {
        LogWriter.log(clientID, "Client server started.") ;
        openServerSocket();
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    LogWriter.log(clientID, "Client server stopped.") ;
                    return;
                }
                throw new RuntimeException("Error accepting replica connection", e);
            }
            new Thread(
                new ServerRunnable(clientID, clientSocket, messageProcessor)
            ).start();
        }
        LogWriter.log(clientID, "Client Stopped.") ;
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stopServer(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + serverPort, e);
        }
    }    
}
