package tdt4140.gr1808.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

public class ClientConnection {
	
	private Socket connectionSocket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	
	public ClientConnection (Socket connectionSocket) throws IOException {
		this.connectionSocket = connectionSocket;
		outputStream = new DataOutputStream(connectionSocket.getOutputStream());
		inputStream = new DataInputStream(connectionSocket.getInputStream());
	}
	
	public void write(JSONObject jsonObject) throws IOException {
		outputStream.writeUTF(jsonObject.toString());
	}
	
	public JSONObject read() throws IOException {
		//Waiting for 0.01s to make sure the data is available on stream (if removed it causes an error sometimes)
		try {
			TimeUnit.SECONDS.sleep(1/100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(inputStream.available() > 0) {
            String jsonObjectString = inputStream.readUTF();
            return new JSONObject(jsonObjectString);
        }
        System.out.println("VI KOMMER HIT");
        return new JSONObject();
	}
	
	public boolean hasNext() throws IOException {
		return (inputStream.available() > 0);
	}
	
	public void close() throws IOException {
		if (!this.isClosed()) {
			connectionSocket.close();
		}
	}
	
	public boolean isClosed() {
		return connectionSocket.isClosed();
	}
}