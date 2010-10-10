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
        
        User u = new User();
        u.setUserID(user_id, password, random);
        
        assertEquals(user_id, u.getUserID(password));
    }
    
    @Test
    public void testGetUserIDWrongPassword() throws NoSuchAlgorithmException {
        String user_id = "deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0deadbeef0";
        String password = "test password";
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        
        User u = new User();
        u.setUserID(user_id, password, random);
        
        assertFalse(user_id == u.getUserID("test pass"));
    }
    
    @Test
    public void testCheckUserPasswordRight() throws NoSuchAlgorithmException {
        String password = "some password";
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        
        User u = new User();
        u.setPassword(password, random);
        assertTrue(u.checkPassword("some password"));
    }
    
    @Test
    public void testCheckUserPasswordWrong() throws NoSuchAlgorithmException {
        String password = "some password";
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        
        User u = new User();
        u.setPassword(password, random);
        assertFalse(u.checkPassword("Other password"));
    }

}
