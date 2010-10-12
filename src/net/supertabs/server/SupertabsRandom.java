package net.supertabs.server;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SupertabsRandom {
    private static final String RANDOM_ALGORITHM = "SHA1PRNG";
    private static SecureRandom random = null;
    
    public static synchronized SecureRandom getSecureRandom() throws NoSuchAlgorithmException {
        if(random == null) {
            random = SecureRandom.getInstance(RANDOM_ALGORITHM);
        }
        
        return random;
    }
    
    
    

}
