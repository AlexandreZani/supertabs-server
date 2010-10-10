package net.supertabs.tests.auth;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

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
    public void testGetUserID() throws NoSuchAlgorithmException {
        String user_id = "deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0";
        String password = "test password";
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        
        User u = new User("username", password, user_id, random);
        
        assertEquals(user_id, u.getUserID(password));
    }
    
    @Test
    public void testGetUserIDWrongPassword() throws NoSuchAlgorithmException {
        String user_id = "deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0";
        String password = "test password";
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        
        User u = new User("username", password, user_id, random);
        
        assertFalse(user_id == u.getUserID("test pass"));
    }
    
    @Test
    public void testCheckUserPasswordRight() throws NoSuchAlgorithmException {
        String password = "some password";
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        
        User u = new User("username", password, "a", random);

        assertTrue(u.checkPassword("some password"));
    }
    
    @Test
    public void testCheckUserPasswordWrong() throws NoSuchAlgorithmException {
        String password = "some password";
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        
        User u = new User("username", password, "a", random);

        assertFalse(u.checkPassword("Other password"));
    }
    
    @Test
    public void testCheckPasswordChange() throws NoSuchAlgorithmException {
        String password = "some password";
        String user_id = "deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0";
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        
        User u = new User("username", password, user_id, random);
        
        assertTrue(u.checkPassword(password));
        assertEquals(user_id, u.getUserID(password));
    }
}
