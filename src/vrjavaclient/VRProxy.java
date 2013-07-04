/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrjavaclient;

/**
 *
 * @author karol
 */
public class VRProxy {
    
    private String severAddress;
    private int serverPort;
    
    public VRProxy(String serverAddress, int serverPort) {
        this.severAddress = serverAddress;
        this.serverPort = serverPort;
    }
    
    public void sendMessage(MessageRequest request) {
        new Thread(new ClientRunnable(severAddress, serverPort, 1, request)).start();
    }

    public String getSeverAddress() {
        return severAddress;
    }

    public void setSeverAddress(String severAddress) {
        this.severAddress = severAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
    
    
}
