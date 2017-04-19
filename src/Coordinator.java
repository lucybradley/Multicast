import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Coordinator {
	
	public static HashMap<Integer, Boolean> clientStatus;
	public static HashMap<Integer, Integer> clientPort;
	public static HashMap<Integer, InetAddress> clientIP;
	public static HashMap<Integer, LinkedList<Message>> savedMsgs;
	public static long thresh;
	
	public static void main(String[] args){
		File config = new File(args[1]);
		
		Scanner scan = null;
		try {
			scan = new Scanner(config);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
			System.exit(0);
		}
		int port = scan.nextInt();
		thresh = scan.nextLong() *1000; //put the threshhold in milliseconds
		scan.close();
		
		ServerSocket serv = null;
		try {
			serv = new ServerSocket(port);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(0);
		}
		
		//wait for connections, spawn threads to deal with those connections
		while(true){
			try {
				CThread c = new CThread(serv.accept());
				c.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
