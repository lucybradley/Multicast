/*
 *
 */
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.InterruptedException;
import java.lang.System;
import java.net.ServerSocket;



public class PListenThread extends Thread  {

    private File logFile;
    private ServerSocket ss;
    
    
    /*
     */
    public PListenThread(int port, String logFileName)  {
        this.logFile = new File(logFileName);
        this.ss = new ServerSocket(port);
    }
    
    
    /*
     */
    public void run()  {
        String message;
        Socket multicastClient = null;
        BufferedReader in = null;
        FileWriter fw = new FileWriter(logFile, true); // append messages to any pre-existing log file content
        
        try  {
            while(true)  {
                try  {
                    multicastClient = ss.accept();
                    in = new BufferedReader(new InputStreamReader(multicastClient.getInputStream()));
                    while(message = in.readLine() != null)  { // ?
                        fw.write(message + "\n");
                    }
                }  catch(IOException e)  {
                    ss.close();
                    System.err.println(e.printStackTrace());
                }
            }
        }  catch(InterruptedException e)  {
            ss.close();
            System.exit(0);
        }
        ss.close();
    }





}