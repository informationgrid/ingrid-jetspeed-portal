/*
 * Copyright (c) 1997-2005 by media style GmbH
 */

package de.ingrid.usermanagement.jetspeed;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.jetspeed.security.SecurityException;
import org.apache.jetspeed.security.UserPrincipal;
import org.apache.jetspeed.security.impl.UserPrincipalImpl;
import org.apache.jetspeed.security.spi.UserSecurityHandler;

import de.ingrid.usermanagement.UserManagement;

/**
 */
public class IngridUserSecurityHandler implements UserSecurityHandler {

    private UserManagement fUserManagement = new UserManagement();

    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#isUserPrincipal(java.lang.String)
     */
    public boolean isUserPrincipal(String userName) {
        verifyUserName(userName);

        return getUserPrincipal(userName) != null;
    }

    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#getUserPrincipal(java.lang.String)
     */
    public Principal getUserPrincipal(String userName) {
        Principal result = null;

        verifyUserName(userName);

        if (this.fUserManagement.userExists(userName)) {
            result = new UserPrincipalImpl(userName);
        }

        return result;
    }

    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#getUserPrincipals(java.lang.String)
     */
    public List getUserPrincipals(String filter) {
        List result = new ArrayList();

        String[] userNames = this.fUserManagement.find(filter);
        for (int i = 0; i < userNames.length; i++) {
            result.add(new UserPrincipalImpl(userNames[i]));
        }

        return result;
    }

    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#addUserPrincipal(org.apache.jetspeed.security.UserPrincipal)
     */
    public void addUserPrincipal(UserPrincipal userPrincipal) throws SecurityException {
        verifyUserPrincipal(userPrincipal);

        String userName = userPrincipal.getName();
        if (isUserPrincipal(userName)) {
            throw new SecurityException(SecurityException.USER_ALREADY_EXISTS.create(userName));
        }

        // FIXME: Add the user here but with its name as the password.
        this.fUserManagement.addUser(userName, userName);
    }

    private void verifyUserPrincipal(UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            throw new IllegalArgumentException("The UserPrincipal cannot be null or empty.");
        }
    }

    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#updateUserPrincipal(org.apache.jetspeed.security.UserPrincipal)
     */
    public void updateUserPrincipal(UserPrincipal userPrincipal) throws SecurityException {
        verifyUserPrincipal(userPrincipal);
        String userName = userPrincipal.getName();
        if (!isUserPrincipal(userName)) {
            addUserPrincipal(userPrincipal);
        }
    }

    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#removeUserPrincipal(org.apache.jetspeed.security.UserPrincipal)
     */
    public void removeUserPrincipal(UserPrincipal userPrincipal) throws SecurityException {
        verifyUserPrincipal(userPrincipal);

        String userName = userPrincipal.getName();

        this.fUserManagement.removeUser(userName);
    }

    private void verifyUserName(String userName) {
        if (StringUtils.isEmpty(userName)) {
            throw new IllegalArgumentException("The user name cannot be null or empty.");
        }
    }

}
