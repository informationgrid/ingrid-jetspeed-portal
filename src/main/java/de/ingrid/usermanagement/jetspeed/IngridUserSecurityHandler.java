/* Copyright 2004 Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.ingrid.usermanagement.jetspeed;

import java.security.Principal;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.jetspeed.security.SecurityException;
import org.apache.jetspeed.security.UserPrincipal;
import org.apache.jetspeed.security.impl.UserPrincipalImpl;
import org.apache.jetspeed.security.om.InternalUserPrincipal;
import org.apache.jetspeed.security.om.impl.InternalUserPrincipalImpl;
import org.apache.jetspeed.security.spi.SecurityAccess;
import org.apache.jetspeed.security.spi.UserSecurityHandler;

/**
 * @see org.apache.jetspeed.security.spi.UserSecurityHandler
 * @author <a href="mailto:dlestrat@apache.org">David Le Strat</a>
 */
public class IngridUserSecurityHandler implements UserSecurityHandler
{
    /** SecurityAccess. */
    private SecurityAccess securityAccess = null;
    
    /**
     * <p>Constructor providing access to the SecurityAccess implementation.</p>
     */
    public IngridUserSecurityHandler()
    {
        this.securityAccess = new SecurityAccessImpl();
    }

    /**
     * Clean up Objects resources. Close the DB Connection. 
     */
    public void destroy() {
        ((SecurityAccessImpl)securityAccess).destroy();
    }
    
    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#isUserPrincipal(java.lang.String)
     */
    public boolean isUserPrincipal(String userName)
    {
        return securityAccess.isKnownUser(userName);
    }
    
    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#getUserPrincipal(java.lang.String)
     */
    public Principal getUserPrincipal(String username)
    {
        UserPrincipal userPrincipal = null;
        InternalUserPrincipal internalUser = securityAccess.getInternalUserPrincipal(username, false);
        if (null != internalUser)
        {
            userPrincipal = new UserPrincipalImpl(UserPrincipalImpl.getPrincipalNameFromFullPath(internalUser.getFullPath()));
            userPrincipal.setEnabled(internalUser.isEnabled());
        }
        return userPrincipal;
    }
    
    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#getUserPrincipals(java.lang.String)
     */
    public List getUserPrincipals(String filter)
    {
        List userPrincipals = new LinkedList();
        Iterator result = securityAccess.getInternalUserPrincipals(filter);
        while (result.hasNext())
        {
            InternalUserPrincipal internalUser = (InternalUserPrincipal) result.next();
            String path = internalUser.getFullPath();
            if (path == null)
            {
                continue;
            }
            UserPrincipal userPrincipal = new UserPrincipalImpl(UserPrincipalImpl.getPrincipalNameFromFullPath(internalUser.getFullPath()));
            userPrincipal.setEnabled(internalUser.isEnabled());
            userPrincipals.add(userPrincipal);
        }
        return userPrincipals;
    }

    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#addUserPrincipal(org.apache.jetspeed.security.UserPrincipal)
     */
    public void addUserPrincipal(UserPrincipal userPrincipal) throws SecurityException
    {
        String fullPath = userPrincipal.getFullPath();
        if ( null == securityAccess.getInternalUserPrincipal(fullPath, false) )
        {
            securityAccess.setInternalUserPrincipal(new InternalUserPrincipalImpl(fullPath), false);        
        }
        else
        {
            throw new SecurityException(SecurityException.USER_ALREADY_EXISTS.create(userPrincipal.getName()));
        }
    }
    
    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#updateUserPrincipal(org.apache.jetspeed.security.UserPrincipal)
     */
    public void updateUserPrincipal(UserPrincipal userPrincipal) throws SecurityException
    {
        String userName = userPrincipal.getName();
        InternalUserPrincipal internalUser = securityAccess.getInternalUserPrincipal(userName, false);
        if ( null != internalUser )
        {
            internalUser.setEnabled(userPrincipal.isEnabled());
            securityAccess.setInternalUserPrincipal(internalUser, false);        
        }
        else
        {
            throw new SecurityException(SecurityException.USER_DOES_NOT_EXIST.create(userPrincipal.getName()));
        }
    }
    
    /**
     * @see org.apache.jetspeed.security.spi.UserSecurityHandler#removeUserPrincipal(org.apache.jetspeed.security.UserPrincipal)
     */
    public void removeUserPrincipal(UserPrincipal userPrincipal) throws SecurityException
    {
        InternalUserPrincipal internalUser = securityAccess.getInternalUserPrincipal(userPrincipal.getName(), false);
        if (null != internalUser)
        {
            securityAccess.removeInternalUserPrincipal(internalUser);
        }
        else
        {
            internalUser = securityAccess.getInternalUserPrincipal(userPrincipal.getName(), true);
            if (null != internalUser)
            {
                securityAccess.removeInternalUserPrincipal(internalUser);
            }
        }
    }

}
