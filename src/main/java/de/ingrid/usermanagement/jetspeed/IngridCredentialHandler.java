/*
 * Copyright (c) 1997-2005 by media style GmbH
 */

package de.ingrid.usermanagement.jetspeed;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jetspeed.security.SecurityException;
import org.apache.jetspeed.security.spi.CredentialHandler;
import org.apache.jetspeed.security.spi.impl.DefaultPasswordCredentialImpl;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.ingrid.usermanagement.HibernateManager;
import de.ingrid.usermanagement.UserManagement;

/**
 */
public class IngridCredentialHandler implements CredentialHandler {

    private Log fLogger = LogFactory.getLog(this.getClass());

    private UserManagement fUserManagement = new UserManagement();

    /**
     * @see org.apache.jetspeed.security.spi.CredentialHandler#getPublicCredentials(java.lang.String)
     */
    public Set getPublicCredentials(String arg0) {
        return new HashSet();
    }

    /**
     * @see org.apache.jetspeed.security.spi.CredentialHandler#getPrivateCredentials(java.lang.String)
     */
    public Set getPrivateCredentials(String userName) {
        Set result = new HashSet();
        String password = null;

        try {
            password = this.fUserManagement.getPassword(userName);

            result.add(new DefaultPasswordCredentialImpl(userName, password.toCharArray()));
        } catch (SecurityException e) {
            this.fLogger.error("Failure creating a PasswordCredential for InternalCredential userName:" + userName, e);
        }

        return result;
    }

    /**
     * @see org.apache.jetspeed.security.spi.CredentialHandler#setPassword(java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    public void setPassword(String userName, String oldPassword, String newPassword) throws SecurityException {
        this.fUserManagement.setPassword(userName, oldPassword, newPassword);
    }

    /**
     * @see org.apache.jetspeed.security.spi.CredentialHandler#setPasswordUpdateRequired(java.lang.String, boolean)
     */
    public void setPasswordUpdateRequired(String arg0, boolean arg1) throws SecurityException {
        // TODO Implement this
    }

    /**
     * @see org.apache.jetspeed.security.spi.CredentialHandler#setPasswordEnabled(java.lang.String, boolean)
     */
    public void setPasswordEnabled(String arg0, boolean arg1) throws SecurityException {
        // TODO Implement this
    }

    /**
     * @see org.apache.jetspeed.security.spi.CredentialHandler#setPasswordExpiration(java.lang.String, java.sql.Date)
     */
    public void setPasswordExpiration(String arg0, Date arg1) throws SecurityException {
        // TODO Implement this
    }

    /**
     * @see org.apache.jetspeed.security.spi.CredentialHandler#authenticate(java.lang.String, java.lang.String)
     */
    public boolean authenticate(String userName, String password) throws SecurityException {
        boolean result = false;

        result = this.fUserManagement.authenticate(userName, password);

        return result;
    }
}
