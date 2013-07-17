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
    MessageProcessor messageProcessor;
    boolean isRunning;
    Scanner scanner;

    public CommandProcessor(Client client, MessageProcessor clientMessageProcessor)
    {
        this.client = client;
        this.messageProcessor = clientMessageProcessor;
        isRunning = true;
        scanner = new Scanner(System.in);
    }

    public void startProcessing()
    {
//        showHelp();
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
                if(commandTokens.length != 3) {
                    procesWrongCommand();
                    continue;
                }
                
                processCopy(commandTokens);
            }
            else if (command.equals("delete"))
            {                
                if(commandTokens.length != 2) {
                    procesWrongCommand();
                    continue;
                }
                
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
    
    public void procesWrongCommand() {
        System.out.println("Wrong command!");
        System.out.println("help -shows commands");
        System.out.println("");
    }
    

    private void processCopy(String[] commandTokens)
    {
        System.out.println("Processing copy.");
        String srcPath = commandTokens[1];
        String destPath = commandTokens[2];
//        byte[] bytes = FileUtility.readFileToByteArray(new File(srcPath));
//        byte[] bytes = FileUtility.readFileToByteArray2(new File(srcPath));
        byte[] bytes = MyFileUtils.readFileToByteArray(new File(srcPath));

        client.incrementRequestsNumber();
        Operation operationCopy = new Operation(destPath, bytes);
        MessageRequest request = new MessageRequest(1, operationCopy, client.getID(), client.getRequestNumber(), client.getViewNumber());
        messageProcessor.sendMessage(request);
    }

    private void processDelete(String[] commandTokens) 
    {
        System.out.println("Processing delete.");
        String destPath = commandTokens[1];
        client.incrementRequestsNumber();
        Operation operationDelete = new Operation(destPath);
        MessageRequest request = new MessageRequest(1, operationDelete, client.getID(), client.getRequestNumber(), client.getViewNumber());
        messageProcessor.sendMessage(request);
    }
}
