package net.supertabs.server.auth;

import java.util.HashMap;

public class SupertabsCredentialsFactory {
    public static SupertabsCredentials getSupertabsCredentials(String type, HashMap<String, String> args, String ip) {
        if (type == UsernamePasswordCredentials.TYPE)
            return new UsernamePasswordCredentials(args, ip);
        else if(type == SessionIdCredentials.TYPE)
            return new SessionIdCredentials(args, ip);
        else
            return null;
    }
}
