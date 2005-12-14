/*
 * Copyright (c) 1997-2005 by media style GmbH
 * 
 * $Source: $
 */

package de.ingrid.usermanagement.jetspeed;

import java.io.Serializable;

import org.apache.jetspeed.security.AuthenticationProvider;
import org.apache.jetspeed.security.impl.AuthenticationProviderImpl;
import org.apache.jetspeed.security.spi.CredentialHandler;
import org.apache.jetspeed.security.spi.UserSecurityHandler;

/**
 */
public class IngridAuthenticationProvider implements AuthenticationProvider {

    private AuthenticationProviderImpl fAuthProv = null;

    private String fId;

    /**
     * @see AuthenticationProviderImpl
     */
    public IngridAuthenticationProvider(String arg1, String arg0, CredentialHandler credentialHandler,
            UserSecurityHandler userSecurityHandler) {
        this.fAuthProv = new AuthenticationProviderImpl(arg0, arg1, credentialHandler, userSecurityHandler);
    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProvider#getProviderName()
     */
    public String getProviderName() {
        return this.fAuthProv.getProviderName();
    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProvider#getProviderDescription()
     */
    public String getProviderDescription() {
        return this.fAuthProv.getProviderDescription();
    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProvider#getUserSecurityHandler()
     */
    public UserSecurityHandler getUserSecurityHandler() {
        return this.fAuthProv.getUserSecurityHandler();
    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProvider#setUserSecurityHandler(org.apache.jetspeed.security.spi.UserSecurityHandler)
     */
    public void setUserSecurityHandler(UserSecurityHandler arg0) {
        this.fAuthProv.setUserSecurityHandler(arg0);
    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProvider#getCredentialHandler()
     */
    public CredentialHandler getCredentialHandler() {
        return this.fAuthProv.getCredentialHandler();
    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProvider#setCredentialHandler(org.apache.jetspeed.security.spi.CredentialHandler)
     */
    public void setCredentialHandler(CredentialHandler arg0) {
        this.fAuthProv.setCredentialHandler(arg0);
    }

    /**
     * @return The id for hibernate.
     */
    private String getId() {
        return this.fId;
    }

    private void setId(String id) {
        this.fId = id;
    }

    private Serializable getAuthenticationProvider() {
        return (Serializable) this.fAuthProv;
    }

    /**
     * @param authenticationProviderImpl
     */
    private void setAuthenticationProvider(Serializable authenticationProviderImpl) {
        this.fAuthProv = (AuthenticationProviderImpl) authenticationProviderImpl;
    }
}
