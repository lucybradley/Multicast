import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class Coordinator {
	
	public static HashMap<Integer, Boolean> clientStatus;
	public static HashMap<Integer, Integer> clientPort;
	public static HashMap<Integer, InetAddress> clientIP;
	public static HashMap<Integer, ArrayList<String>> saveMsgs;
}
