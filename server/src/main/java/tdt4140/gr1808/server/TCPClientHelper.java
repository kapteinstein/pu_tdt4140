package tdt4140.gr1808.server;

import java.net.Socket;

public class TCPClientHelper extends Thread {
	Socket connection;

	TCPClientHelper(Socket connection) {
		this.connection = connection;
	}

	@Override
	public void run() {
	}
}
