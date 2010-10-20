package net.supertabs.server.requests;

import java.util.HashMap;

import net.supertabs.server.auth.AuthenticationDatabase;
import net.supertabs.server.auth.InvalidCredentialsException;
import net.supertabs.server.auth.SupertabsCredentials;

public class PingRequest implements SupertabsRequest {
    public static final String TYPE = "Ping";
    private SupertabsCredentials credentials;
    
    public PingRequest(HashMap<String, String> args, SupertabsCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public String getActionMethod() {
        return TYPE;
    }

    @Override
    public String getCredentialsType() {
        return this.credentials.getCredentialsType();
    }

    @Override
    public String Execute(AuthenticationDatabase auth_db) {
        try {
            this.credentials.getUserId(auth_db);
        } catch (InvalidCredentialsException e) {
            return "<supertabs>" + e.getResponse() + "</supertabs>";
        }
        
        String ret = "";
        ret += "<supertabs>";
        ret += this.credentials.getResponse();
        ret += "<response><type>" + TYPE + "</type></response>";
        ret += "</supertabs>";
        return ret;
    }
}
