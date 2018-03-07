package tdt4140.gr1808.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

	@After
	public void tearDown() {
		if (!server.socketIsClosed())
			server.close();
	}
}
