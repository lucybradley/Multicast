package coordinator;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class CThread extends Thread{
	
	private Socket sock;
	private OutputStream writer = null;
	private InputStream reader = null;
	
	public CThread(Socket sock){
		this.sock = sock;
	}

	@Override
	public void run(){
		System.out.println("Connection from client received");
		
		try {
			writer = sock.getOutputStream();
			reader = sock.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		byte[] buf = new byte[1024];
		int readIn = 0;
		while(readIn==0){
			try {
				readIn = reader.read(buf);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println( readIn + "bytes read from socket");
		
		String command = new String(buf, 0, readIn);
		try {
			writer.write("OK".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		parseInput(command);
	}
	
	public void parseInput(String input){
		System.out.println("String received: " + input);
		Scanner scan = new Scanner(input);
		scan.useDelimiter(" ");
		String command = scan.next();
        try{
            switch(command){
                case "register":
                    register(scan.nextInt(), InetAddress.getByName(scan.next()), scan.nextInt());
                    break;
                case "deregister":
                    int x = Integer.valueOf(command.substring(command.indexOf(' ')+1));
                    deregister(x);
                    break;
                case "disconnect":
                    int y = Integer.valueOf(command.substring(command.indexOf(' ')+1));
                    disconnect(y);
                    break;
                case "reconnect":
                	reconnect(scan.nextInt(), InetAddress.getByName(scan.next()), scan.nextInt());
                	break;
                case "msend":
                    msend(command);
                    break;
                default:
                    System.out.println("Invalid input from participant");                
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        scan.close();
	}
	
	public void register(int id, InetAddress addr, int port){
		System.out.println("id= " + id);
		System.out.println("adress= " + addr.getHostAddress());
		System.out.println("port= " + port);

		Coordinator.clientIP.put(id, addr);
	    Coordinator.clientStatus.put(id, true);
	    Coordinator.clientPort.put(id, port);
	    
	    System.out.println("Client " + id + "registered on port " + port);
	}
	
	public void deregister(int id){
		Coordinator.clientIP.remove(id);
	    Coordinator.clientStatus.remove(id);
	    Coordinator.clientPort.remove(id);
	    Coordinator.savedMsgs.remove(id);
	    
	    System.out.println("Client " + id + "deregistered");
	}
	
	public void disconnect(int id){
	    Coordinator.clientStatus.replace(id, false);
	    
	    System.out.println("Client " + id + "disconnected");
	}
	
	public void reconnect(int id, InetAddress addr, int port){
		Coordinator.clientStatus.replace(id, true);
		Coordinator.clientIP.replace(id, addr);
		Coordinator.clientPort.replace(id,  port);
		
	    System.out.println("Client " + id + "reconnected on port " + port);
	}
	
	public void msend(String msg){
		
	}
}
