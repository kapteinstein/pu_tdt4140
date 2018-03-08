package tdt4140.gr1808.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *	A basic TCP server that runs on port 6789
 *	Creates a DBQuery object and a Parser object
 *	Passes them and the input and output stream to a S&R object that is created for every connection
 *
 * 	clients can connect using:
 * 		Socket client = new Socket(serverName, port);
 */

public class TCPServer extends Thread {
	private ServerSocket serverSocket;
	private ServerParser serverParser;
	private DBQuery dbquery;

	public TCPServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		serverParser = new ServerParser();
		dbquery = new DBQuery();
	}

	public void run() {
		while (!socketIsClosed()) {
			try {
				System.out.println("Waiting for client on port " +
					serverSocket.getLocalPort() + "...");
				Socket connectionSocket = serverSocket.accept();
				System.out.println("Connected to " +
					connectionSocket.getRemoteSocketAddress());

				BufferedReader input = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream output = new DataOutputStream(connectionSocket.getOutputStream());

				ClientConnect clientConnect = new ClientConnect(dbquery, serverParser,
					input, output, connectionSocket);
				Thread c = new Thread(clientConnect);
				c.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public int close() {
		try {
			serverSocket.close();
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 1;
		}
	}

	public boolean socketIsClosed() {
		return serverSocket.isClosed();
	}

	public static void main(String[] args) {
		int port = 6789;
		try {
			Thread t = new TCPServer(port);
			t.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
