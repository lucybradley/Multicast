import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class CThread extends Thread{
	
	private Socket sock;
	private OutputStream writer;
	private InputStream reader;
	
	public CThread(Socket sock){
		this.sock = sock;
	}

	@Override
	public void run(){
		//run
	}
}
