/*
 * Copyright (c) 1997-2005 by media style GmbH
 * 
 * $Source: $
 */

package de.ingrid.usermanagement.jetspeed;

import java.sql.Date;
import java.util.Collection;
import java.util.Iterator;

import org.apache.jetspeed.security.SecurityException;
import org.apache.jetspeed.security.User;
import org.apache.jetspeed.security.UserManager;

import de.ingrid.usermanagement.UserManagement;

/**
 *  
 */
public class IngridUserManager implements UserManager {

    private UserManagement fUserManagement;

    /**
     * @param um
     */
    public IngridUserManager(UserManagement um) {
        this.fUserManagement = um;
    }

    /**
     * @see org.apache.jetspeed.security.UserManager#getAnonymousUser()
     */
    public String getAnonymousUser() {
        // TODO implement
        return null;
    }

    /**
     * @see org.apache.jetspeed.security.UserManager#authenticate(java.lang.String, java.lang.String)
     */
    public boolean authenticate(String username, String password) {
        return this.fUserManagement.authenticate(username, password);
    }

    /**
     * @see org.apache.jetspeed.security.UserManager#addUser(java.lang.String, java.lang.String)
     */
    public void addUser(String username, String password) throws SecurityException {
        this.fUserManagement.addUser(username, password);
    }

    /**
     * @see org.apache.jetspeed.security.UserManager#addUser(java.lang.String, java.lang.String, java.lang.String)
     */
    public void addUser(String arg0, String arg1, String arg2) throws SecurityException {
        // TODO implement
    }

    /**
     * @see org.apache.jetspeed.security.UserManager#removeUser(java.lang.String)
     */
    public void removeUser(String username) throws SecurityException {
        this.fUserManagement.removeUser(username);
    }

    /**
     * @see org.apache.jetspeed.security.UserManager#userExists(java.lang.String)
     */
    public boolean userExists(String username) {
        return this.fUserManagement.userExists(username);
    }

    /**
     * @see org.apache.jetspeed.security.UserManager#getUser(java.lang.String)
     */
    public User getUser(String arg0) throws SecurityException {
        //      TODO implement
        return null;
    }

    /**
     * @see org.apache.jetspeed.security.UserManager#getUsers(java.lang.String)
     */
    public Iterator getUsers(String arg0) throws SecurityException {
        //      TODO implement
        return null;
    }

    /**
     * @see org.apache.jetspeed.security.UserManager#getUsersInRole(java.lang.String)
     */
    public Collection getUsersInRole(String arg0) throws SecurityException {
        //      TODO implement
        return null;
    }

    /**
     * @see org.apache.jetspeed.security.UserManager#getUsersInGroup(java.lang.String)
     */
    public Collection getUsersInGroup(String arg0) throws SecurityException {
        //      TODO implement
        return null;
    }

    /**
     * @see org.apache.jetspeed.security.UserManager#setPassword(java.lang.String, java.lang.String, java.lang.String)
     */
    public void setPassword(String username, String oldPassword, String newPassword) throws SecurityException {
        this.fUserManagement.setPassword(username, oldPassword, newPassword);
    }

    /**
     * @see org.apache.jetspeed.security.UserManager#setPasswordUpdateRequired(java.lang.String, boolean)
     */
    public void setPasswordUpdateRequired(String arg0, boolean arg1) throws SecurityException {
        //      TODO implement
    }

    /**
     * @see org.apache.jetspeed.security.UserManager#setPasswordEnabled(java.lang.String, boolean)
     */
    public void setPasswordEnabled(String arg0, boolean arg1) throws SecurityException {
        //      TODO implement
    }

    /**
     * @see org.apache.jetspeed.security.UserManager#setUserEnabled(java.lang.String, boolean)
     */
    public void setUserEnabled(String arg0, boolean arg1) throws SecurityException {
        //      TODO implement
    }

    /**
     * @see org.apache.jetspeed.security.UserManager#setPasswordExpiration(java.lang.String, java.sql.Date)
     */
    public void setPasswordExpiration(String arg0, Date arg1) throws SecurityException {
        //      TODO implement
    }
}
