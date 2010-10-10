package net.supertabs.server.auth;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class User {
    private static final int SALT_SZ = 256;
    private static final String HASH_ALGO = "SHA-256";
    private String username;
    private BigInteger salted_password;
    private BigInteger password_salt;
    private BigInteger encrypted_uid;
    private BigInteger uid_salt;
    
    public User() {
    }
    
    public User(String username, String password, String user_id, SecureRandom random) throws NoSuchAlgorithmException {
        this.username = username;
        this.setUserID(user_id, password, random);
        this.setPassword(password, random);
    }
    
    public void setPassword(String password, SecureRandom random) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[User.SALT_SZ/8];
        random.nextBytes(bytes);
        this.password_salt = new BigInteger(1, bytes);
        
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGO);
        digest.reset();
        digest.update(password.getBytes());
        digest.update(this.password_salt.toByteArray());
        this.salted_password = new BigInteger(digest.digest());
    }
    
    public boolean checkPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(password.getBytes());
        digest.update(this.password_salt.toByteArray());
        return this.salted_password.compareTo(new BigInteger(digest.digest())) == 0;
    }
    
    public void setUserID(String user_id, String password, SecureRandom random) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[User.SALT_SZ/8];
        random.nextBytes(bytes);
        this.uid_salt = new BigInteger(1, bytes);
        
        BigInteger uid = new BigInteger(user_id, 16);

        this.encrypted_uid = this.CryptUIDKey(uid, password);
    }
    
    public String getUserID(String password) throws NoSuchAlgorithmException {
        return this.CryptUIDKey(this.encrypted_uid, password).toString(16);
    }
    
    private BigInteger CryptUIDKey(BigInteger uid, String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(password.getBytes());
        digest.update(this.uid_salt.toString(16).getBytes());
        
        BigInteger key = new BigInteger(1, digest.digest());
        
        return uid.xor(key);
    }
}
