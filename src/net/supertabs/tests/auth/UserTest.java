package net.supertabs.tests.auth;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;

import net.supertabs.server.auth.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetUserID() {
        String user_id = "deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0";
        String password = "test password";
        
        User u = new User("username", password, user_id);
        
        assertEquals(user_id, u.getUserID(password));
    }
    
    @Test
    public void testGetUserIDWrongPassword() {
        String user_id = "deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0";
        String password = "test password";
        
        User u = new User("username", password, user_id);
        
        assertFalse(user_id == u.getUserID("test pass"));
    }
    
    @Test
    public void testCheckUserPasswordRight() {
        String password = "some password";
        
        User u = new User("username", password, "a");

        assertTrue(u.checkPassword("some password"));
    }
    
    @Test
    public void testCheckUserPasswordWrong() {
        String password = "some password";
        
        User u = new User("username", password, "a");

        assertFalse(u.checkPassword("Other password"));
    }
    
    @Test
    public void testCheckPasswordChange() {
        String password = "some password";
        String user_id = "deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0";
        
        User u = new User("username", password, user_id);
        
        assertTrue(u.checkPassword(password));
        assertEquals(user_id, u.getUserID(password));
    }
    
    @Test
    public void testUserCopy() {
        String password = "some password";
        String user_id = "deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0";
        
        User u = new User("username", password, user_id);
        
        User u2 = new User(u);
        
        assertTrue(u.equals(u2));
    }
}
