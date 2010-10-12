package net.supertabs.tests.auth;


import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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

}