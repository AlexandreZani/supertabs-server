package net.supertabs.server.auth;

public interface SupertabsCredentials {
    public boolean Authenticate(AuthenticationDatabase db);
    public String getResponse();
    public String getUserId();
    public String getCredentialsType();
}
