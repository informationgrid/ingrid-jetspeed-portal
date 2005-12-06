/*
 * Created on 05.12.2005
 */
package de.ingrid.usermanagement.jetspeed;

import org.apache.jetspeed.security.AuthenticationProvider;
import org.apache.jetspeed.security.spi.CredentialHandler;
import org.apache.jetspeed.security.spi.UserSecurityHandler;

/**
 * 
 */
public class IngridAuthenticationProvider implements AuthenticationProvider {

    /**
     * @see org.apache.jetspeed.security.AuthenticationProvider#getProviderName()
     */
    public String getProviderName() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProvider#getProviderDescription()
     */
    public String getProviderDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProvider#getUserSecurityHandler()
     */
    public UserSecurityHandler getUserSecurityHandler() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProvider#setUserSecurityHandler(org.apache.jetspeed.security.spi.UserSecurityHandler)
     */
    public void setUserSecurityHandler(UserSecurityHandler arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProvider#getCredentialHandler()
     */
    public CredentialHandler getCredentialHandler() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProvider#setCredentialHandler(org.apache.jetspeed.security.spi.CredentialHandler)
     */
    public void setCredentialHandler(CredentialHandler arg0) {
        // TODO Auto-generated method stub

    }

}
