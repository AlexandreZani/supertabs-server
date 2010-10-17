package net.supertabs.server;

public class SupertabsException extends Exception {
    protected String error_string;
    
    public SupertabsException() {
        // TODO Auto-generated constructor stub
    }

    public SupertabsException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public SupertabsException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    public SupertabsException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }
    
    public String getResponse() {
        String ret = "<response><type>Error</type><arguments>";
        ret += "<name>" + this.error_string + "</name>";
        ret += "<msg>" + this.getMessage() + "</msg></response>";
        return ret;
    }
}
