package net.supertabs.server.auth;

import java.util.HashMap;

public class UsernamePasswordCredentials implements SupertabsCredentials {
    private String username;
    private String password;
    private String ip;
    private String response = null;
    public final static String TYPE = "UsernamePassword";
    
    public UsernamePasswordCredentials(HashMap<String, String> args, String ip) {
        this.username = args.get("username");
        this.password = args.get("password");
        this.ip = ip;
    }

    @Override
    public String getUserId(AuthenticationDatabase db) throws InvalidCredentialsException {
        if(this.username == null || this.password == null) {
            throw new InvalidCredentialsException("Missing username or password!");
        }
        
        User u = db.getUser(this.username);
        if(!u.checkPassword(this.password)) {
            throw new InvalidCredentialsException("Invalid username password combination!");
        }
        
        String uid = u.getUserID(this.password); 
        
        String sid = db.newSession(this.ip, uid);
        
        this.response = "<credentials><method>SessionID</method><arguments><session_id>" + sid + "</session_id></arguments></credentials>";
        
        return uid;
    }

    @Override
    public String getResponse() {
        return this.response;
    }

    @Override
    public String getCredentialsType() {
        return TYPE;
    }
}
