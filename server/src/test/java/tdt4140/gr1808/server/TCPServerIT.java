package tdt4140.gr1808.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.*;

class TCPClient {
	Socket connection = null;
	DataOutputStream out;
	BufferedReader in;

	TCPClient(int port) throws IOException {
		connection = new Socket("localhost", port);
	}

	public String connectStatus() {
		return String.valueOf(connection.getRemoteSocketAddress());
	}

	public Socket getConnection() {
		return connection;
	}

	public void generateIn() throws IOException {
		in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	}

	public void generateOut() throws IOException {
		out = new DataOutputStream(connection.getOutputStream());
	}

	public void write(String data) throws IOException {
		out.writeUTF(data);
		System.out.println("data written: " + data);
	}
}

public class TCPServerIT {
	TCPServer server = null;

	@Before
	public void setUp() {
		int port = 6789;
		try {
			server = new TCPServer(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void constructorTest() {
		assertTrue(server != null);
	}

	@Test
	public void socketOpenClosedTest() {
		assertFalse(server.socketIsClosed());
	}

	@Test
	public void runTest() {
		server.start();
		assertEquals(0, server.close());
	}

	@Test
	public void clientConnectSuccessTest() {
		int port = 6789;
		TCPClient client = null;

		try {
			client = new TCPClient(port);
			assertTrue(client.getConnection() != null);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

	}

	@Test
	public void clientConnectFailTest() {
		int port = 6789;
		TCPClient client = null;
		server.close();
		// the server is now closed
		try {
			client = new TCPClient(port);
			fail();
		} catch (IOException e) {
		}
	}

	@Test
	public void clientInputSteamTest() {
		int port = 6789;
		TCPClient client = null;
		try {
			client = new TCPClient(port);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		try {
			client.generateIn();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void clientOutputSteamTest() {
		int port = 6789;
		TCPClient client = null;
		try {
			client = new TCPClient(port);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		try {
			client.generateOut();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void clientServerInteractionTest() {
		int port = 6789;
		String data = "test data";
		TCPClient client = null;
		server.start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			client = new TCPClient(port);
			client.generateIn();
			client.generateOut();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		try {
			client.write(data);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	@After
	public void tearDown() {
		if (!server.socketIsClosed())
			server.close();
		try {
			Thread.sleep(500);  // just to be sure
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
