package net.supertabs.server.auth;

import java.util.HashMap;

public class SessionIdCredentials implements SupertabsCredentials {
    private String session_id;
    private String ip;
    public final static String TYPE = "SessionId";
    
    public SessionIdCredentials(HashMap<String, String> args, String ip) {
        this.session_id = args.get("session_id");
        this.ip = ip;
    }

    @Override
    public String getUserId(AuthenticationDatabase db) throws InvalidCredentialsException {
        if(this.ip == null || this.session_id == null)
            throw new InvalidCredentialsException("No session id was provided.");
        
        String uid;
        if((uid = db.checkSession(ip, this.session_id)) == null)
            throw new InvalidCredentialsException("Invalid session id.");
        
        return uid;
    }

    @Override
    public String getResponse() {
        return "";
    }

    @Override
    public String getCredentialsType() {
        return TYPE;
    }

}
