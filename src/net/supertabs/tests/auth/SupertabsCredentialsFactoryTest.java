package net.supertabs.tests.auth;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;

import net.supertabs.server.auth.AuthenticationDatabase;
import net.supertabs.server.auth.SupertabsCredentials;
import net.supertabs.server.auth.SupertabsCredentialsFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SupertabsCredentialsFactoryTest {
    private Connection conn;
    private AuthenticationDatabase db;
    
    @Before
    public void setUp() throws Exception {
        this.conn = DriverManager.getConnection("jdbc:mysql://localhost/SupertabsDB", "test", "password");
        this.db = new AuthenticationDatabase(conn);
        Statement stmt = this.conn.createStatement();
        stmt.execute("DELETE From Users");
        stmt.execute("DELETE From Sessions");
    }

    @After
    public void tearDown() throws Exception {
        Statement stmt = this.conn.createStatement();
        stmt.execute("DELETE From Users");
        stmt.execute("DELETE From Sessions");
        this.conn.close();
    }
    
    @Test
    public void testUsernamePass() {
        this.db.newUser("alex", "pass", "deadbeef");
        HashMap<String, String> args = new HashMap<String, String>();
        args.put("username", "alex");
        args.put("password", "pass");
        
        SupertabsCredentials cred = SupertabsCredentialsFactory.getSupertabsCredentials("UsernamePassword", args, "127.0.0.1");
        assertTrue(cred.Authenticate(db));
    }
    
    @Test
    public void testUsernamePassFail() {
        this.db.newUser("alex", "pass", "deadbeef");
        HashMap<String, String> args = new HashMap<String, String>();
        args.put("username", "alex");
        args.put("password", "passs");
        
        SupertabsCredentials cred = SupertabsCredentialsFactory.getSupertabsCredentials("UsernamePassword", args, "127.0.0.1");
        assertFalse(cred.Authenticate(db));
    }
    
    @Test
    public void testSessionId() {
        String session_id = this.db.newSession("127.0.0.1", "deadbeef");
        
        HashMap<String, String> args = new HashMap<String, String>();
        args.put("session_id", session_id);
        
        SupertabsCredentials cred = SupertabsCredentialsFactory.getSupertabsCredentials("SessionId", args, "127.0.0.1");
        
        assertTrue(cred.Authenticate(this.db));
    }
    
    @Test
    public void testSessionIdFail() {
        this.db.newSession("127.0.0.1", "deadbeef");
        
        HashMap<String, String> args = new HashMap<String, String>();
        args.put("session_id", "deadbeef");
        
        SupertabsCredentials cred = SupertabsCredentialsFactory.getSupertabsCredentials("SessionId", args, "127.0.0.1");
        
        assertFalse(cred.Authenticate(this.db));
    }
}
