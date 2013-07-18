/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrjavaclient;

import java.util.TimerTask;

/**
 *
 * @author Karol
 */
public class MyTimerTask extends TimerTask{
    
    Client client;
    MessageRequest request;
    
    public MyTimerTask(Client client, MessageRequest request) {
        super();
        this.client = client;
        this.request = request;
    }

    @Override
    public void run() {
        client.getMessageProcessor().sendToAll(request);
    }
    
}
