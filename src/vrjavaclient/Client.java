/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrjavaclient;

import java.util.ArrayList;

/**
 *
 * @author karol
 */
public class Client {
    
    private static final String PARAMETER_FILE_NAME = "Parameters.txt";
    private static final String HOSTS_FILE_NAME = "Hosts.txt";
    private static final String NEWLINE = System.getProperty("line.separator");
    
    private int clientID;
    private String ipAddress;
    private int port;
    private ReplicaInfo primary;
    private ReplicaTable replicaTable;
    private int viewNumber;
    private int requestNumber;
    private CommandProcessor commandProcessor;
    private VRProxy vrProxy;
    private MessageProcessor messageProcessor;
    
    public Client() {
        replicaTable = new ReplicaTable();
        loadParameters();
        loadHosts();
        primary = replicaTable.get(0);
        viewNumber = 1;
        requestNumber = 0;
        System.out.println(identify());
        vrProxy = new VRProxy();
        commandProcessor = new CommandProcessor(this, null);
        //messageProcessor = new MessageProcessor();
        
        commandProcessor.startProcessing();
    }
    
    private String identify() {
        String s = "";
        s += "Client App" + NEWLINE;
        s += "ID: " + clientID + NEWLINE;
        s += "ipAddress: " + ipAddress + NEWLINE;
        s += "port: " + port + NEWLINE;
        s += "Hosts: " + NEWLINE;
        for(ReplicaInfo rep : replicaTable) {
            s += "Replica[" + rep.getReplicaID() + "] " + rep.getIpAddress() + ":" + rep.getPort() + NEWLINE;
        }
        return s;
    }
    
    private boolean loadParameters() {
        ArrayList<ArrayList<String>> tokenizedLines = FileUtility.loadFile(PARAMETER_FILE_NAME);
        if(tokenizedLines.size() >= 3) {
            int repID = Integer.valueOf(tokenizedLines.get(0).get(0));
            String address = tokenizedLines.get(1).get(0);
            int portNumber = Integer.valueOf(tokenizedLines.get(2).get(0));
            this.clientID = repID;
            this.ipAddress = address;
            this.port = portNumber;
            return true;
        } else {
            return false;
        }
    }
    
    public boolean  testLoadParameters() {
        return loadParameters();
    }
    
    private boolean loadHosts() {
        ArrayList<ArrayList<String>> tokenizedLines = FileUtility.loadFile(HOSTS_FILE_NAME);
        if(tokenizedLines.size() > 0) {
            for(int i = 0; i < tokenizedLines.size(); i++) {
                ArrayList<String> tokens = tokenizedLines.get(i);
                if(tokens.size() >= 2) {
                    int replicaID = i+1;
                    String address = tokens.get(0);                
                    int port = Integer.valueOf(tokens.get(1));
                    replicaTable.add(new ReplicaInfo(replicaID, address, port));
                }
            }
        } 
        boolean result = (replicaTable.size() > 0) ? true : false;
        return result;
    }
    
    public boolean  testloadHosts() {
        return loadHosts();
    }
    
    public void incrementRequestsNumber() {
        requestNumber++;
    }

    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ReplicaInfo getPrimary() {
        return primary;
    }

    public void setPrimary(ReplicaInfo primary) {
        this.primary = primary;
    }

    public ReplicaTable getReplicaTable() {
        return replicaTable;
    }

    public void setReplicaTable(ReplicaTable replicaTable) {
        this.replicaTable = replicaTable;
    }

    public int getViewNumber() {
        return viewNumber;
    }

    public void setViewNumber(int viewNumber) {
        this.viewNumber = viewNumber;
    }

    public int getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(int requestNumber) {
        this.requestNumber = requestNumber;
    }
    
    
    
}
