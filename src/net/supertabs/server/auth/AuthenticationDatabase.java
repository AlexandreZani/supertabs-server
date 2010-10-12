package net.supertabs.server.auth;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import net.supertabs.server.SupertabsRandom;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class AuthenticationDatabase {
    private Connection conn;
    private long session_life = 24*60*60*1000;
    
    public AuthenticationDatabase(Connection c) {
        this.conn = c;
    }
    
    public User getUser(String username) throws SQLException, NoSuchAlgorithmException {
        String sql = "SELECT UserName, SaltedPassword, PasswordSalt, EncryptedUserId, UserIdSalt From Users WHERE UserName=?";
        
        PreparedStatement stmt = this.conn.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.execute();
        
        ResultSet results = stmt.getResultSet();
        
        if(!results.next())
            return null;
        
        return new User(results.getString(1), results.getString(2),
                results.getString(3), results.getString(4), results.getString(5));
    }
    
    public void newUser(String username, String password, String user_id) throws NoSuchAlgorithmException, SQLException {
        this.writeUser(new User(username, password, user_id));
    }
    
    public void writeUser(User u) throws SQLException {
        String sql = "INSERT INTO Users (UserName, SaltedPassword, PasswordSalt, EncryptedUserId, UserIdSalt) VALUES(?, ?, ?, ?, ?)";
        
        PreparedStatement stmt = this.conn.prepareStatement(sql);
        stmt.setString(1, u.getUsername());
        stmt.setString(2, u.getSaltedPassword());
        stmt.setString(3, u.getPasswordSalt());
        stmt.setString(4, u.getEncryptedUserID());
        stmt.setString(5, u.getUserIDSalt());
        
        try {
            stmt.execute();
        } catch(MySQLIntegrityConstraintViolationException e) {
            sql = "UPDATE Users SET SaltedPassword=?, PasswordSalt=?, EncryptedUserId=?, UserIdSalt=? WHERE UserName=?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, u.getSaltedPassword());
            stmt.setString(2, u.getPasswordSalt());
            stmt.setString(3, u.getEncryptedUserID());
            stmt.setString(4, u.getUserIDSalt());
            stmt.setString(5, u.getUsername());
            stmt.execute();
        }
    }
    
    public String newSession(String username, String password, String ip) throws NoSuchAlgorithmException, SQLException {
        User u = this.getUser(username);
        if(!u.checkPassword(password))
            return null;
        
        String uid = u.getUserID(password);
        return this.newSession(ip, uid);
    }
    
    public String newSession(String ip, String user_id) throws NoSuchAlgorithmException, SQLException {
        String sql = "INSERT INTO Sessions (SessionId, IP, UserId, LastTouched) VALUES(?, ?, ?, ?)";
        boolean failed;
        
        PreparedStatement stmt;
        BigInteger session_id;
        byte[] bytes = new byte[User.SALT_SZ/8];
        
        do {
            failed = false;
            SupertabsRandom.getSecureRandom().nextBytes(bytes);
            session_id = new BigInteger(1, bytes);
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, session_id.toString(16));
            stmt.setString(2, ip);
            stmt.setString(3, user_id);
            stmt.setLong(4, Calendar.getInstance().getTimeInMillis());
            try {
                stmt.execute();
            } catch(MySQLIntegrityConstraintViolationException e) {
                failed = true;
            }
        } while(failed);
        
        return session_id.toString(16);
    }
    
    public String checkSession(String ip, String session_id) throws SQLException {
        String user_id;
        
        String sql = "SELECT UserId FROM Sessions WHERE SessionId=? AND IP=? AND LastTouched>?";
        PreparedStatement stmt;
        stmt = this.conn.prepareStatement(sql);
        stmt.setString(1, session_id);
        stmt.setString(2, ip);
        stmt.setLong(3, Calendar.getInstance().getTimeInMillis()-this.session_life);
        
        stmt.execute();
        
        ResultSet results = stmt.getResultSet();
        
        if(!results.next()) {
            sql = "DELETE FROM Sessions WHERE SessionId=? AND IP=?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, session_id);
            stmt.setString(2, ip);
            
            stmt.execute();
            return null;
        }
        
        user_id = results.getString("UserId");
        
        sql = "UPDATE Sessions SET LastTouched=? WHERE SessionId=? AND IP=?";
        stmt = this.conn.prepareStatement(sql);
        stmt.setLong(1, Calendar.getInstance().getTimeInMillis());
        stmt.setString(2, session_id);
        stmt.setString(3, ip);
        
        stmt.execute();

        return user_id;
    }
    
    public void deleteSession(String ip, String session_id) throws SQLException {
        String sql = "DELETE FROM Sessions WHERE SessionId=? AND IP=?";
        PreparedStatement stmt = this.conn.prepareStatement(sql);
        stmt.setString(1, session_id);
        stmt.setString(2, ip);
        stmt.execute();
    }

    public void setSessionLife(long session_life) {
        this.session_life = session_life;
    }

    public long getSessionLife() {
        return session_life;
    }
}
