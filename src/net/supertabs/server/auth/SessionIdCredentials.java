package net.supertabs.server.auth;

import java.util.HashMap;

public class SessionIdCredentials implements SupertabsCredentials {
    private String session_id;
    private String ip;
    private String uid = null;
    public final static String TYPE = "SessionId";
    
    public SessionIdCredentials(HashMap<String, String> args, String ip) {
        this.session_id = args.get("session_id");
        this.ip = ip;
    }

    @Override
    public boolean Authenticate(AuthenticationDatabase db) {
        if(this.ip == null || this.session_id == null)
            return false;
        
        if((this.uid = db.checkSession(ip, this.session_id)) == null)
            return false;
        
        return true;
    }

    @Override
    public String getResponse() {
        return "";
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
