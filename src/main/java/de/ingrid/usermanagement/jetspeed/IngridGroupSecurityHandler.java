/*
 * Copyright (c) 1997-2006 by wemove GmbH
 */
package de.ingrid.usermanagement.jetspeed;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.jetspeed.security.GroupPrincipal;
import org.apache.jetspeed.security.SecurityException;
import org.apache.jetspeed.security.impl.GroupPrincipalImpl;
import org.apache.jetspeed.security.spi.GroupSecurityHandler;

import de.ingrid.usermanagement.UserManagement;

/**
 * TODO Describe your created type (class, etc.) here.
 *
 * @author joachim@wemove.com
 */
public class IngridGroupSecurityHandler implements GroupSecurityHandler {

    private UserManagement fUserManagement = new UserManagement();

    /**
     * @see org.apache.jetspeed.security.spi.GroupSecurityHandler#getGroupPrincipal(java.lang.String)
     */
    public Principal getGroupPrincipal(String groupName) {
        GroupPrincipal groupPrincipal = null;
        
        verifyGroupName(groupName);
        
        if (fUserManagement.groupExists(groupName)) {
            groupPrincipal = new GroupPrincipalImpl(groupName);
        }
        return groupPrincipal;
    }

    /**
     * @see org.apache.jetspeed.security.spi.GroupSecurityHandler#setGroupPrincipal(org.apache.jetspeed.security.GroupPrincipal)
     */
    public void setGroupPrincipal(GroupPrincipal groupPrincipal) throws SecurityException {

        verifyGroupName(groupPrincipal.getName());

        if (!fUserManagement.groupExists(groupPrincipal.getName())) {
            fUserManagement.addGroup(groupPrincipal.getName());
        }

    }

    /**
     * @see org.apache.jetspeed.security.spi.GroupSecurityHandler#removeGroupPrincipal(org.apache.jetspeed.security.GroupPrincipal)
     */
    public void removeGroupPrincipal(GroupPrincipal groupPrincipal) throws SecurityException {

        verifyGroupName(groupPrincipal.getName());
        
        if (!fUserManagement.groupExists(groupPrincipal.getName())) {
            fUserManagement.removeGroup(groupPrincipal.getName());
        }

    }

    /**
     * @see org.apache.jetspeed.security.spi.GroupSecurityHandler#getGroupPrincipals(java.lang.String)
     */
    public List getGroupPrincipals(String filter) {
        List groupPrincipals = new ArrayList();
        
        String[] groupNames = this.fUserManagement.findGroups(filter);
        for (int i = 0; i < groupNames.length; i++) {
            groupPrincipals.add(new GroupPrincipalImpl(groupNames[i]));
        }        
        
        return groupPrincipals;    
    }

    private void verifyGroupName(String groupName) {
        if (StringUtils.isEmpty(groupName)) {
            throw new IllegalArgumentException("The group name cannot be null or empty.");
        }
    }
    
}
