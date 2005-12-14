/*
 * Copyright (c) 1997-2005 by media style GmbH
 * 
 * $Source: $
 */

package de.ingrid.usermanagement.jetspeed;

import java.sql.Date;
import java.util.Set;

import org.apache.jetspeed.security.SecurityException;
import org.apache.jetspeed.security.spi.CredentialHandler;

/**
 * TODO: implement this
 */
public class IngridCredentialHandler implements CredentialHandler {

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
