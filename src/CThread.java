import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	
	public void parseInput(String command){
		
	}
	
	public void deregister(int id){
		
	}
}
