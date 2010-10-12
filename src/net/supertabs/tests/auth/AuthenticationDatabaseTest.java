package net.supertabs.tests.auth;


import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import net.supertabs.server.auth.AuthenticationDatabase;
import net.supertabs.server.auth.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AuthenticationDatabaseTest {

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
    public void DummyTest() {
        assertTrue(true);
    }
    
    @Test
    public void testUnknownUser() throws NoSuchAlgorithmException, SQLException {
        User u2 = this.db.getUser("username");
        
        assertEquals(null, u2);
    }
    
    @Test
    public void testWriteUser() throws NoSuchAlgorithmException, SQLException {
        String password = "some password";
        String user_id = "deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0";
        
        User u = new User("username", password, user_id);
        
        this.db.writeUser(u);
        
        User u2 = this.db.getUser("username");
        
        assertTrue(u.equals(u2));
    }
    
    @Test
    public void testChangeUser() throws NoSuchAlgorithmException, SQLException {
        String password = "some password";
        String user_id = "deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0";
        
        User u = new User("username", password, user_id);
        
        this.db.writeUser(u);
        
        u.changePassword(password, "cats");
        
        this.db.writeUser(u);
        
        User u2 = this.db.getUser("username");
        
        assertTrue(u.equals(u2));
    }
    
    @Test
    public void testSession() throws NoSuchAlgorithmException, SQLException {
        String user_id = "deadbeef";
        String ip = "127.0.0.1";
        String session_id = this.db.newSession(ip, user_id);
        
        String ret_uid = this.db.checkSession(ip, session_id);
        
        assertEquals(user_id, ret_uid);
    }
    
    @Test
    public void testSessionWrongIP() throws NoSuchAlgorithmException, SQLException {
        String user_id = "deadbeef";
        String ip = "127.0.0.1";
        String session_id = this.db.newSession(ip, user_id);
        
        String ret_uid = this.db.checkSession("127.0.0.2", session_id);
        
        assertEquals(null, ret_uid);
    }
    
    @Test
    public void testSessionWrongSession() throws NoSuchAlgorithmException, SQLException {
        String user_id = "deadbeef";
        String ip = "127.0.0.1";
        String session_id = this.db.newSession(ip, user_id);
        
        String ret_uid = this.db.checkSession(ip, "a1231ab");
        
        assertEquals(null, ret_uid);
    }
    
    @Test
    public void testSessionDelete() throws NoSuchAlgorithmException, SQLException {
        String user_id = "deadbeef";
        String ip = "127.0.0.1";
        String session_id = this.db.newSession(ip, user_id);
        
        this.db.deleteSession(ip, session_id);
        
        String ret_uid = this.db.checkSession(ip, session_id);
        
        assertEquals(null, ret_uid);
    }
}
