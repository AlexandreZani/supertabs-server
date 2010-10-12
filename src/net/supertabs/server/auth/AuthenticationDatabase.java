package net.supertabs.server.auth;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import net.supertabs.server.SupertabsRandom;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class AuthenticationDatabase {
    private Connection conn;
    
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
}
