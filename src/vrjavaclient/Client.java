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
    
    private int ID;
    private String serverIpAddress;
    private int serverPort;
    private ReplicaInfo primary;
    private ReplicaTable replicaTable;
    private ClientTable clientTable;
    private int viewNumber;
    private int requestNumber;
    private CommandProcessor commandProcessor;
    private MessageProcessor messageProcessor;
    private VRProxy vrProxy;
    //private MessageProcessor messageProcessor;
    
    public Client() {
        replicaTable = new ReplicaTable();
        clientTable = new ClientTable();
        loadAndSetParameters();
        System.out.println(identify());
        
        messageProcessor = new MessageProcessor(this);
        vrProxy = new VRProxy(ID, serverPort, messageProcessor);
        vrProxy.start();
        commandProcessor = new CommandProcessor(this, messageProcessor);
        commandProcessor.startProcessing();
    }
    
    private String identify() {
        String s = "";
        s += "Client " + ID + Constants.NEWLINE;
        s += "Server IP: " + serverIpAddress + Constants.NEWLINE;
        s += "Server port: " + serverPort + Constants.NEWLINE;
        s += "Hosts: " + Constants.NEWLINE;
        for(ReplicaInfo rep : replicaTable) {
            s += "Replica[" + rep.getReplicaID() + "] " + rep.getIpAddress() + ":" + rep.getPort() + Constants.NEWLINE;
        }
        return s;
    }
    
    private void loadAndSetParameters() {
        loadHosts();
        loadClients();
        loadParameters();
        setParameters();
        
        primary = replicaTable.get(0);
        viewNumber = 1;
        requestNumber = 0;
    }
    
    private boolean loadHosts() {
        ArrayList<ArrayList<String>> tokenizedLines = MyFileUtils.loadFile(Constants.HOSTS_FILE_NAME);
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
    
    private boolean loadClients() {
        ArrayList<ArrayList<String>> tokenizedLines = MyFileUtils.loadFile(Constants.CLIENTS_FILE_NAME);
        if(tokenizedLines.size() > 0) {
            for(int i = 0; i < tokenizedLines.size(); i++) {
                ArrayList<String> tokens = tokenizedLines.get(i);
                if(tokens.size() >= 2) {
                    int clientID = i+1;
                    String address = tokens.get(0);                
                    int port = Integer.valueOf(tokens.get(1));
                    clientTable.add(new ClientInfo(clientID, address, port));
                }
            }
        } 
        boolean result = (clientTable.size() > 0) ? true : false;
        return result;
    }
    
    private boolean loadParameters() {
        ArrayList<ArrayList<String>> tokenizedLines = MyFileUtils.loadFile(Constants.PARAMETER_FILE_NAME);
        if(tokenizedLines.size() >= 1) {
            int id = Integer.valueOf(tokenizedLines.get(0).get(0));
            this.ID = id;
            return true;
        } else {
            return false;
        }
    }
    
    public boolean  testLoadParameters() {
        return loadParameters();
    }
    
    private void setParameters() {
        int index = ID-1;
        this.serverIpAddress = clientTable.get(index).getIpAddress();
        this.serverPort = clientTable.get(index).getPort();
    }
    
    public void incrementRequestsNumber() {
        requestNumber++;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getServerIpAddress() {
        return serverIpAddress;
    }

    public void setServerIpAddress(String serverIpAddress) {
        this.serverIpAddress = serverIpAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
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
