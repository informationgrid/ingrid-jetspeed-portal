/*
 * Copyright (c) 1997-2005 by media style GmbH
 * 
 * $Source: $
 */

package de.ingrid.usermanagement.jetspeed;

import java.security.Principal;
import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.apache.jetspeed.security.AuthenticationProviderProxy;
import org.apache.jetspeed.security.SecurityException;
import org.apache.jetspeed.security.UserPrincipal;

/**
 * 
 */
public class IngridAuthenticationProviderProxy implements AuthenticationProviderProxy {

    /**
     * @see org.apache.jetspeed.security.AuthenticationProviderProxy#getDefaultAuthenticationProvider()
     */
    public String getDefaultAuthenticationProvider() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProviderProxy#getAuthenticationProvider(java.lang.String)
     */
    public String getAuthenticationProvider(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProviderProxy#addUserPrincipal(org.apache.jetspeed.security.UserPrincipal, java.lang.String)
     */
    public void addUserPrincipal(UserPrincipal arg0, String arg1) throws SecurityException {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProviderProxy#updateUserPrincipal(org.apache.jetspeed.security.UserPrincipal, java.lang.String)
     */
    public void updateUserPrincipal(UserPrincipal arg0, String arg1) throws SecurityException {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProviderProxy#removeUserPrincipal(org.apache.jetspeed.security.UserPrincipal, java.lang.String)
     */
    public void removeUserPrincipal(UserPrincipal arg0, String arg1) throws SecurityException {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProviderProxy#setPassword(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public void setPassword(String arg0, String arg1, String arg2, String arg3) throws SecurityException {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProviderProxy#setPasswordUpdateRequired(java.lang.String, boolean, java.lang.String)
     */
    public void setPasswordUpdateRequired(String arg0, boolean arg1, String arg2) throws SecurityException {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProviderProxy#setPasswordEnabled(java.lang.String, boolean, java.lang.String)
     */
    public void setPasswordEnabled(String arg0, boolean arg1, String arg2) throws SecurityException {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProviderProxy#setPasswordExpiration(java.lang.String, java.sql.Date, java.lang.String)
     */
    public void setPasswordExpiration(String arg0, Date arg1, String arg2) throws SecurityException {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.apache.jetspeed.security.AuthenticationProviderProxy#authenticate(java.lang.String, java.lang.String, java.lang.String)
     */
    public boolean authenticate(String arg0, String arg1, String arg2) throws SecurityException {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#isUserPrincipal(java.lang.String)
     */
    public boolean isUserPrincipal(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#getUserPrincipal(java.lang.String)
     */
    public Principal getUserPrincipal(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#getUserPrincipals(java.lang.String)
     */
    public List getUserPrincipals(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#addUserPrincipal(org.apache.jetspeed.security.UserPrincipal)
     */
    public void addUserPrincipal(UserPrincipal arg0) throws SecurityException {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#updateUserPrincipal(org.apache.jetspeed.security.UserPrincipal)
     */
    public void updateUserPrincipal(UserPrincipal arg0) throws SecurityException {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#removeUserPrincipal(org.apache.jetspeed.security.UserPrincipal)
     */
    public void removeUserPrincipal(UserPrincipal arg0) throws SecurityException {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.apache.jetspeed.security.spi.CredentialHandler#getPublicCredentials(java.lang.String)
     */
    public Set getPublicCredentials(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.apache.jetspeed.security.spi.CredentialHandler#getPrivateCredentials(java.lang.String)
     */
    public Set getPrivateCredentials(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.apache.jetspeed.security.spi.CredentialHandler#setPassword(java.lang.String, java.lang.String, java.lang.String)
     */
    public void setPassword(String arg0, String arg1, String arg2) throws SecurityException {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.apache.jetspeed.security.spi.CredentialHandler#setPasswordUpdateRequired(java.lang.String, boolean)
     */
    public void setPasswordUpdateRequired(String arg0, boolean arg1) throws SecurityException {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.apache.jetspeed.security.spi.CredentialHandler#setPasswordEnabled(java.lang.String, boolean)
     */
    public void setPasswordEnabled(String arg0, boolean arg1) throws SecurityException {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.apache.jetspeed.security.spi.CredentialHandler#setPasswordExpiration(java.lang.String, java.sql.Date)
     */
    public void setPasswordExpiration(String arg0, Date arg1) throws SecurityException {
        // TODO Auto-generated method stub

    }

    /**
     * @see org.apache.jetspeed.security.spi.CredentialHandler#authenticate(java.lang.String, java.lang.String)
     */
    public boolean authenticate(String arg0, String arg1) throws SecurityException {
        // TODO Auto-generated method stub
        return false;
    }

}
