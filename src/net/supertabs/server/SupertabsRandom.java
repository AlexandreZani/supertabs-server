package net.supertabs.server;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SupertabsRandom {
    private static final String RANDOM_ALGORITHM = "SHA1PRNG";
    private static SecureRandom random = null;
    
    public static synchronized SecureRandom getSecureRandom() {
        if(random == null) {
            try {
                random = SecureRandom.getInstance(RANDOM_ALGORITHM);
            } catch (NoSuchAlgorithmException e) {
                throw new Error(e);
            }
        }
        
        return random;
    }
    
    
    

}
