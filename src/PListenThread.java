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
import java.net.Socket;



public class PListenThread extends Thread  {

    private File logFile;
    private ServerSocket ss;
    
    
    /*
     */
    public PListenThread(int port, String logFileName)  {
        this.logFile = new File(logFileName);
        try {
			this.ss = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
    }
    
    
    /*
     */
    public void run()  {
        String message;
        Socket multicastClient = null;
        BufferedReader in = null;
        FileWriter fw = null;
		try {
			fw = new FileWriter(logFile, true);
		} catch (IOException e1) {
			e1.printStackTrace();
		} // append messages to any pre-existing log file content
        
        try  {
            while(true)  {
                try  {
                    multicastClient = ss.accept();
                    in = new BufferedReader(new InputStreamReader(multicastClient.getInputStream()));
                    while((message = in.readLine()) != null)  { // ?
                        fw.write(message + "\n");
                    }
                }  catch(IOException e)  {
                    try {
						ss.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
                    e.printStackTrace();
                }
            }
        }  catch(InterruptedException e)  {
            try {
				ss.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
            System.exit(0);
        }
        try {
			ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }





}