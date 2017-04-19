import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Participant {

	private int id;
	private int sPort;
	private InetAddress addr = null;
	private PListenThread listen;
	private String logFile;
	private OutputStream writer = null;
	private InputStream reader = null;
	
	public Participant(String filename){
		File config = new File(filename);
		Scanner scan = null;
		try {
			scan = new Scanner(config);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		//parse config file
		id = Integer.valueOf(scan.nextLine());
		logFile = scan.nextLine();
		String IPPort = scan.nextLine();
		
		try {
			addr = InetAddress.getByName(IPPort.substring(0, IPPort.indexOf(':')));
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(0);
		}
		sPort = Integer.valueOf(IPPort.substring(IPPort.indexOf(':')+1).trim());
		scan.close();
	}
	
	public void parseInput(String input){
		String command = input.substring(0, input.indexOf(' '));
		
		try{
			switch(command){
				case "register":
					register(Integer.valueOf(command.substring(command.indexOf(' ')+1)));
					break;
				case "deregister":
					deregister();
					break;
				case "disconnect":
					disconnect();
					break;
				case "reconnect":
					reconnect(Integer.valueOf(command.substring(command.indexOf(' ')+1)));
					break;
				case "msend":
					msend(command);
					break;
				default:
					System.out.println("Invalid input, try again");					
			}
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public Socket createSocket(){
		//create socket to coordinator
		Socket sock = null;
		try {
			sock = new Socket(addr, sPort);
			writer = sock.getOutputStream();
			reader = sock.getInputStream();
		} catch (IOException e) {
			System.out.println("Connection failed");
		}
		
		return sock;
	}
	
	public boolean sendAndReceive(String msg) throws IOException{
		//send message to coordinator
		writer.write(msg.getBytes());
		
		//wait for acknowledgement
		byte[] buf = new byte["OK".getBytes().length];
		reader.read(buf); //wait for longer??
		
		if(!new String(buf).equals("OK")){
			System.out.println("Coordinator did not acknowledge, try again");
			return false;
		}
		
		return true;
	}
	
	public void register(int lPort) throws IOException{
		//create listening thread
		listen = new PListenThread(lPort, logFile);
		listen.start();
		
		Socket sock =createSocket();
		
		String register = "register " + id + " " + addr.getHostAddress() + " " + lPort;
		
		if(!sendAndReceive(register)){
			listen.interrupt();
			listen = null;
			System.out.println("Register failed, try again");
			return;
		}
		
		sock.close();
		
	}
	
	public void deregister() throws IOException{
		if(listen == null){
			System.out.println("Register first!");
			return;
		}
		
		Socket sock = createSocket();
		
		if(!sendAndReceive("deregister " + id));{
			System.out.println("Deregister failed, try again");
		}
		
		//kill listening thread so a new one can be created with next register
		listen.interrupt();
		listen = null;
		
		sock.close();
	}
	
	public void disconnect() throws IOException{
		if(listen == null){
			System.out.println("Register first!");
			return;
		}
		
		Socket sock = createSocket();
		
		if(!sendAndReceive("disconnect " + id));{
			System.out.println("Disconnect failed, try again");
		}
		
		//interrupt listening thread so a new one can be created with next connect
		listen.interrupt();
		
		sock.close();
	}
	
	public void reconnect(int lPort) throws IOException{
		if(listen == null){
			System.out.println("Register first!");
			return;
		}
		
		listen = new PListenThread(lPort, logFile);
		listen.start();
		
		Socket sock = createSocket();
		
		if(!sendAndReceive("reconnect " + id + " " + addr.getHostAddress() + " " + lPort));{
			System.out.println("Reconnect failed, try again");
		}
		
		sock.close();
	}
	
	public void msend(String msg) throws IOException{
		Socket sock = createSocket();
		
		if(!sendAndReceive(msg));{
			System.out.println("Msend failed, try again");
		}
		
		sock.close();
	}
	
	public static void main(String[] args){
		Participant p = new Participant(args[1]);
		Scanner scan = new Scanner(System.in);
		
		while(true){
			p.parseInput(scan.nextLine());
		}
	}
}
