package coordinator;

public class Message {
	private long timestamp;
	private String msg;
	
	public Message(long timestamp, String msg){
		this.timestamp = timestamp;
		this.msg = msg;
	}
	
	public long getTimestamp(){
		return timestamp;
	}
	
	public String getMsg(){
		return msg;
	}
}
