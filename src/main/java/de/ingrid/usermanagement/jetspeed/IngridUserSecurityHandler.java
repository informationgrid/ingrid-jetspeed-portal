/*
 * Created on 05.12.2005
 */
package de.ingrid.usermanagement.jetspeed;

import java.security.Principal;
import java.util.List;

import org.apache.jetspeed.security.SecurityException;
import org.apache.jetspeed.security.UserPrincipal;
import org.apache.jetspeed.security.spi.UserSecurityHandler;

/**
 * 
 */
public class IngridUserSecurityHandler implements UserSecurityHandler {

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

}
