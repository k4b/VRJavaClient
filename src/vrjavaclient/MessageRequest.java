/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrjavaclient;

/**
 *
 * @author karol
 */
public class MessageRequest extends Message {
    
    private static final String NEWLINE = System.getProperty("line.separator");
    
    int clientID;
    int requestNumber;
    int viewNumber;
    Operation operation;
    
    public MessageRequest(int messageID, int clientID, int requestNumber, int viewNumber, Operation operation) {
        this.setMessageID(messageID);
        this.clientID = clientID;
        this.requestNumber = requestNumber;
        this.viewNumber = viewNumber;
        this.operation = operation;
    }
    
    @Override
    public String toString() {
        String s = "";
        s += "Message REQUEST" + NEWLINE;
        s += "ID: " + getMessageID() + NEWLINE;
        s += "Operation: " + operation.getOperationID() + NEWLINE;
        s += "Request number: " + requestNumber + NEWLINE;
        s += "View number: " + viewNumber + NEWLINE;
        return s;
    }
}
