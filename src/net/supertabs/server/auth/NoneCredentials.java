package net.supertabs.server.auth;

public class NoneCredentials implements SupertabsCredentials {
    public final static String TYPE = "None";

    @Override
    public String getCredentialsType() {
        return TYPE;
    }

    @Override
    public String getResponse() {
        return "";
    }

    @Override
    public String getUserId(AuthenticationDatabase db)
            throws InvalidCredentialsException {
        throw new InvalidCredentialsException("Credentials type unknown or no credentials provided.");
    }

}
