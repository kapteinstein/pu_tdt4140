package tdt4140.gr1808.server;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
	// private ServerParser serverParser;
	//Opprett DBQuery-objekt

	private TCPServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		// serverParser = new ServerParser();
	}

	public void run() {
		while (!serverSocket.isClosed()) {
			try {
				System.out.println("Waiting for client on port " +
					serverSocket.getLocalPort() + "...");
				Socket connectionSocket = serverSocket.accept();
				System.out.println("Connected to " +
					connectionSocket.getRemoteSocketAddress());

				InputStream input = connectionSocket.getInputStream();
				OutputStream output = connectionSocket.getOutputStream();

				//Opprett sendAndRecieve-objekt
				//sendAndRecieve(DBQuery, serverParser, input, output)

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
