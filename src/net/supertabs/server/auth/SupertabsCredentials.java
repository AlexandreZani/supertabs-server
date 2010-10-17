package net.supertabs.server.auth;

public interface SupertabsCredentials {
    public String getUserId(AuthenticationDatabase db) throws InvalidCredentialsException;
    public String getResponse();
    public String getCredentialsType();
}
