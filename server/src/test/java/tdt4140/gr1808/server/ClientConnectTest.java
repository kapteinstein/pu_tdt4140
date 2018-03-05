import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import static org.mockito.Mockito;


public class ClientConnectTest {
    @before
    public void setUp() {
        ServerParser severParser = new ServerParser();
        DBQuery dbQuery;

    }
    @Test
    public void TestConnectAddUser() {
      ClientConnect tester = new ClientConnect();

      dbQuery = mock(DBQuery.class);
      when(dbQuery.addUser("Per").thenReturn("Flotters!");
      AssertTrue("Flotters!".equals(tester.))


        // assert statements
        assertEquals()
    }
}
