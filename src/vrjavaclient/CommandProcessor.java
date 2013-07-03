package vrjavaclient;

import java.io.File;
import java.util.Scanner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author karol
 */
public class CommandProcessor {
    
    Client client;
    VRProxy proxy;
    boolean isRunning;
    Scanner scanner;

    public CommandProcessor(Client client, VRProxy proxy)
    {
        this.client = client;
        this.proxy = proxy;
        isRunning = true;
        scanner = new Scanner(System.in);
    }

    public void startProcessing()
    {
        showHelp();
        while (isRunning)
        {
            askCommand();
            String line = scanner.nextLine();
            System.out.println();
            String[] commandTokens = line.split(" ");

            String command = commandTokens[0];
            if (command.equals("exit"))
            {
                return;
            }
            else if (command.equals("help"))
            {
                showHelp();
            }
            else if(command.equals("copy"))
            {
                commandTokens = new String[]
                {
                    "copy",
                    "C:\\hosts.txt",
                    "hosts.txt"
                };
                processCopy(commandTokens);
            }
            else if (command.equals("delete"))
            {
                commandTokens = new String[]
                {
                    "delete",
                    "hosts.txt"
                };
                processDelete(commandTokens);
            }
            else
            {
                System.out.println("NO SUCH COMMAND!");
                System.out.println();
                showHelp();
            }
            System.out.println();
        }
    }

    public void stopProcessing()
    {
        isRunning = false;
    }

    public void showHelp()
    {
        System.out.println("#Commands:");
        System.out.println("#Copy: copy src_file dest_file_in_root");
        System.out.println("#Delete: delete file_path");
        System.out.println("#Help: help");
        System.out.println("#Exit: exit");
        System.out.println("#Full paths to files");
    }

    public void askCommand()
    {
        System.out.println("Enter command:");
        System.out.println("");
        System.out.print(">");
    }
    

    private void processCopy(String[] commandTokens)
    {
        System.out.println("Copy");
        String srcPath = commandTokens[1];
        String destPath = commandTokens[2];
        byte[] bytes = FileUtility.readFileToByteArray(new File(srcPath));

        client.incrementRequestsNumber();
        Operation operationCopy = new Operation(destPath, bytes);
        MessageRequest request = new MessageRequest(1, client.getClientID(), client.getRequestNumber(), client.getViewNumber(), operationCopy);
        System.out.println("Sending:");
        System.out.println(request.toString());
        //proxy.startClient();
//        proxy.sendMessage(request);
    }

    private void processDelete(String[] commandTokens) 
    {
//        String destPath = commandTokens[1];
//        client.incrementRequestsNumber();
//        Operation operationDelete = new Operation(destPath);
//        MessageRequest request = new MessageRequest(1, operationDelete, client.ID, client.requestNumber, client.viewNumber);
//        System.out.println("Sending:");
//        System.out.println(request.ToString());
//        proxy.sendMessage(request);
    }
}
