import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class CThread extends Thread{
	
	private Socket sock;
	private OutputStream writer = null;
	private InputStream reader = null;
	
	public CThread(Socket sock){
		this.sock = sock;
	}

	@Override
	public void run(){
		try {
			writer = sock.getOutputStream();
			reader = sock.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		byte[] buf = new byte[1024];
		int readIn = 0;
		try {
			readIn = reader.read(buf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String command = new String(buf, 0, readIn);
		try {
			writer.write("OK".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		parseInput(command);
	}
	
	public void parseInput(String input){
		String command = input.substring(0, input.indexOf(' '));
	    input = input.substring(input.indexOf(' ')+1);
	    String idString;
	    String addr;
	    String portString;
	            try{
	                switch(command){
	                    case "register":
	                        idString= input.substring(0,input.indexOf(' '));
	                        addr= input.substring(input.indexOf(' ') +1, input.lastIndexOf(' '));
	                        portString= input.substring(input.lastIndexOf(' ')+1);
	                        register(Integer.valueOf(idString), InetAddress.getByName(addr), Integer.valueOf(portString));
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
	                        idString= input.substring(0,input.indexOf(' '));
	                        addr= input.substring(input.indexOf(' ') +1, input.lastIndexOf(' '));
	                        portString= input.substring(input.lastIndexOf(' ')+1);
	                        reconnect(Integer.valueOf(idString), InetAddress.getByName(addr), Integer.valueOf(portString));                     break;
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
	
	public void register(int id, InetAddress addr, int port){
		Coordinator.clientIP.put(id, addr);
	    Coordinator.clientStatus.put(id, true);
	    Coordinator.clientPort.put(id, port);
	}
	
	public void deregister(int id){
		Coordinator.clientIP.remove(id);
	    Coordinator.clientStatus.remove(id);
	    Coordinator.clientPort.remove(id);
	    Coordinator.savedMsgs.remove(id);
	}
	
	public void disconnect(int id){
	    Coordinator.clientStatus.replace(id, false);
	}
	
	public void reconnect(int id, InetAddress addr, int port){
		Coordinator.clientStatus.replace(id, true);
		Coordinator.clientIP.replace(id, addr);
		Coordinator.clientPort.replace(id,  port);
	}
	
	public void msend(String msg){
		
	}
}
