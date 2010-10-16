package net.supertabs.server.auth;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.supertabs.server.SupertabsRandom;

public class User {
    public static final int SALT_SZ = 256;
    private static final String HASH_ALGO = "SHA-256";
    private String username;
    private BigInteger salted_password;
    private BigInteger password_salt;
    private BigInteger encrypted_uid;
    private BigInteger uid_salt;
    
    public String getSaltedPassword() {
        return this.salted_password.toString(16);
    }
    
    public String getPasswordSalt() {
        return this.password_salt.toString(16);
    }
    
    public String getEncryptedUserID() {
        return this.encrypted_uid.toString(16);
    }
    
    public String getUserIDSalt() {
        return this.uid_salt.toString(16);
    }
    
    public User(User u) {
        this(u.getUsername(), u.getSaltedPassword(), u.getPasswordSalt(), u.getEncryptedUserID(), u.getUserIDSalt());
    }
    
    public User(String username, String salted_password, String password_salt, String encrypted_uid, String uid_salt) {
        this.username = username;
        this.salted_password = new BigInteger(salted_password, 16);
        this.password_salt = new BigInteger(password_salt, 16);
        this.encrypted_uid = new BigInteger(encrypted_uid, 16);
        this.uid_salt = new BigInteger(uid_salt, 16);
    }
    
    public boolean equals(User u) {
        return u.getUsername().equals(this.username) &&
            u.getEncryptedUserID().equals(this.getEncryptedUserID()) &&
            u.getPasswordSalt().equals(this.getPasswordSalt()) &&
            u.getSaltedPassword().equals(this.getSaltedPassword()) &&
            u.getUserIDSalt().equals(this.getUserIDSalt());
    }
    
    public User(String username, String password, String user_id) {
        this.username = username;
        this.setUserID(user_id, password);
        this.setPassword(password);
    }

    public void setPassword(String password) {
        try {
            byte[] bytes = new byte[User.SALT_SZ/8];
            SupertabsRandom.nextBytes(bytes);
            this.password_salt = new BigInteger(1, bytes);
            
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGO);
            digest.reset();
            digest.update(password.getBytes());
            digest.update(this.password_salt.toByteArray());
            this.salted_password = new BigInteger(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new Error(e);
        }
    }
    
    public void changePassword(String old_password, String new_password) {
        BigInteger uid = this.CryptUIDKey(this.encrypted_uid, old_password);
        this.setUserID(uid.toString(16), new_password);
        this.setPassword(new_password);
    }
    
    public boolean checkPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGO);
            digest.reset();
            digest.update(password.getBytes());
            digest.update(this.password_salt.toByteArray());
            return this.salted_password.equals(new BigInteger(digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new Error(e);
        }
    }
    
    public void setUserID(String user_id, String password) {
        byte[] bytes = new byte[User.SALT_SZ/8];
        SupertabsRandom.nextBytes(bytes);
        this.uid_salt = new BigInteger(1, bytes);
        
        BigInteger uid = new BigInteger(user_id, 16);
   
        this.encrypted_uid = this.CryptUIDKey(uid, password);
    }
    
    public String getUserID(String password) {
        return this.CryptUIDKey(this.encrypted_uid, password).toString(16);
    }
    
    private BigInteger CryptUIDKey(BigInteger uid, String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGO);
            digest.reset();
            digest.update(password.getBytes());
            digest.update(this.uid_salt.toString(16).getBytes());
            
            BigInteger key = new BigInteger(1, digest.digest());
            
            return uid.xor(key);
        } catch (NoSuchAlgorithmException e) {
            throw new Error(e);
        }
    }

    public String getUsername() {
        return username;
    }
}
