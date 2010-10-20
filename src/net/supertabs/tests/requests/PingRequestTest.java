package net.supertabs.tests.requests;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;

import net.supertabs.server.auth.AuthenticationDatabase;
import net.supertabs.server.auth.SupertabsCredentials;
import net.supertabs.server.auth.SupertabsCredentialsFactory;
import net.supertabs.server.requests.PingRequest;
import net.supertabs.server.requests.SupertabsRequest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PingRequestTest {

    private Connection conn;
    private AuthenticationDatabase db;
    
    @Before
    public void setUp() throws Exception {
        this.conn = DriverManager.getConnection("jdbc:mysql://localhost/SupertabsDB", "test", "password");
        this.db = new AuthenticationDatabase(conn);
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE From Users");
        stmt.execute("DELETE From Sessions");
    }

    @After
    public void tearDown() throws Exception {
        this.conn.close();
    }
    
    @Test
    public void testPingRequest() {
        SupertabsCredentials credentials = SupertabsCredentialsFactory.getSupertabsCredentials("", new HashMap<String, String>(), "");
        SupertabsRequest request = new PingRequest(null, credentials);
        request.Execute(this.db);
    }
}
