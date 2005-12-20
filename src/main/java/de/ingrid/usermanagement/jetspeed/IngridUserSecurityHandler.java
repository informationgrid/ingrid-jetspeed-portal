/*
 * Copyright (c) 1997-2005 by media style GmbH
 */

package de.ingrid.usermanagement.jetspeed;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jetspeed.security.SecurityException;
import org.apache.jetspeed.security.UserPrincipal;
import org.apache.jetspeed.security.impl.UserPrincipalImpl;
import org.apache.jetspeed.security.spi.UserSecurityHandler;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.ingrid.usermanagement.HibernateUtil;
import de.ingrid.usermanagement.UserManagement;

/**
 */
public class IngridUserSecurityHandler implements UserSecurityHandler {

    private final Log fLogger = LogFactory.getLog(this.getClass());

    private Session fSession;

    /**
     */
    public IngridUserSecurityHandler() {
        this.fSession = HibernateUtil.currentSession();
    }

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

        Transaction tx = this.fSession.beginTransaction();
        List authList = this.fSession.createQuery("from UserManagement").list();
        try {
            UserManagement um = (UserManagement) authList.get(0);

            if (um.userExists(userName)) {
                result = new UserPrincipalImpl(userName);
            }
        } catch (IndexOutOfBoundsException e) {
            this.fLogger.error("An hibernate error has occurred: No UserManagement found.");
        } finally {
            tx.commit();
        }

        return result;
    }

    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#getUserPrincipals(java.lang.String)
     */
    public List getUserPrincipals(String filter) {
        List result = new ArrayList();
        
        Transaction tx = this.fSession.beginTransaction();
        List authList = this.fSession.createQuery("from UserManagement").list();
        try {
            UserManagement um = (UserManagement) authList.get(0);
            
            String[] userNames = um.find(filter);
            for (int i = 0; i < userNames.length; i++) {
                result.add(new UserPrincipalImpl(userNames[i]));
            }
        } catch (IndexOutOfBoundsException e) {
            this.fLogger.error("An hibernate error has occurred: No UserManagement found.");
        } finally {
            tx.commit();
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

        Transaction tx = this.fSession.beginTransaction();
        List authList = this.fSession.createQuery("from UserManagement").list();
        try {
            UserManagement um = (UserManagement) authList.get(0);

            // FIXME: Add the user here but with its name as the password.
            um.addUser(userName, userName);
            this.fSession.update(um);
        } catch (IndexOutOfBoundsException e) {
            this.fLogger.error("An hibernate error has occurred: No UserManagement found.");
        } finally {
            tx.commit();
        }
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

        Transaction tx = this.fSession.beginTransaction();
        List authList = this.fSession.createQuery("from UserManagement").list();
        try {
            UserManagement um = (UserManagement) authList.get(0);

            um.removeUser(userName);
            this.fSession.update(um);
        } catch (IndexOutOfBoundsException e) {
            this.fLogger.error(e);
            throw new SecurityException(SecurityException.UNEXPECTED
                    .create("An hibernate error has occurred: No UserManagement found."));
        } finally {
            tx.commit();
        }
    }

    private void verifyUserName(String userName) {
        if (StringUtils.isEmpty(userName)) {
            throw new IllegalArgumentException("The user name cannot be null or empty.");
        }
    }

}
