package net.supertabs.server.auth;

import java.util.HashMap;

public class UsernamePasswordCredentials implements SupertabsCredentials {
    private String username;
    private String password;
    private String ip;
    private String response = null;
    private String uid = null;
    public final static String TYPE = "UsernamePassword";
    
    public UsernamePasswordCredentials(HashMap<String, String> args, String ip) {
        this.username = args.get("username");
        this.password = args.get("password");
        this.ip = ip;
    }

    @Override
    public boolean Authenticate(AuthenticationDatabase db) {
        if(this.username == null || this.password == null) {
            return false;
        }
        
        User u = db.getUser(this.username);
        if(!u.checkPassword(this.password)) {
            return false;
        }
        
        this.uid = u.getUserID(this.password);
        
        String sid = db.newSession(this.ip, this.uid);
        
        this.response = "<credentials><method>SessionID</method><arguments><session_id>" + sid + "</session_id></arguments></credentials>";
        
        return true;
    }

    @Override
    public String getResponse() {
        return this.response;
    }

    @Override
    public String getUserId() {
        return this.uid;
    }

    @Override
    public String getCredentialsType() {
        return TYPE;
    }
}
