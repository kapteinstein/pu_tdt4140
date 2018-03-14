package tdt4140.gr1808.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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
        if(inputStream.available() > 0) {
            String jsonObjectString = inputStream.readUTF();
            return new JSONObject(jsonObjectString);
        }   
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