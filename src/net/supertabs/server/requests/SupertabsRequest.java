package net.supertabs.server.requests;

import net.supertabs.server.auth.AuthenticationDatabase;

public interface SupertabsRequest {
    public String getActionMethod();
    public String getCredentialsType();
    public String Execute(AuthenticationDatabase auth_db);
}
