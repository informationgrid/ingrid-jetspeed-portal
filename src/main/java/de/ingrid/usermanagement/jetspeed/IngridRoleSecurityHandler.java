/*
 * Copyright (c) 1997-2006 by wemove GmbH
 */
package de.ingrid.usermanagement.jetspeed;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.jetspeed.security.RolePrincipal;
import org.apache.jetspeed.security.SecurityException;
import org.apache.jetspeed.security.impl.RolePrincipalImpl;
import org.apache.jetspeed.security.spi.RoleSecurityHandler;

import de.ingrid.usermanagement.UserManagement;

/**
 * TODO Describe your created type (class, etc.) here.
 *
 * @author joachim@wemove.com
 */
public class IngridRoleSecurityHandler implements RoleSecurityHandler {

    private UserManagement fUserManagement = new UserManagement();
    
    /**
     * @see org.apache.jetspeed.security.spi.RoleSecurityHandler#getRolePrincipal(java.lang.String)
     */
    public Principal getRolePrincipal(String roleName) {
        
        RolePrincipal rolePrincipal = null;
        
        verifyRoleName(roleName);
        
        if (fUserManagement.roleExists(roleName)) {
            rolePrincipal = new RolePrincipalImpl(roleName);
        }
        return rolePrincipal;
    }

    /**
     * @see org.apache.jetspeed.security.spi.RoleSecurityHandler#setRolePrincipal(org.apache.jetspeed.security.RolePrincipal)
     */
    public void setRolePrincipal(RolePrincipal rolePrincipal) throws SecurityException {
        
        verifyRoleName(rolePrincipal.getName());
        
        if (!fUserManagement.roleExists(rolePrincipal.getName())) {
            fUserManagement.addRole(rolePrincipal.getName());
        }

    }

    /**
     * @see org.apache.jetspeed.security.spi.RoleSecurityHandler#removeRolePrincipal(org.apache.jetspeed.security.RolePrincipal)
     */
    public void removeRolePrincipal(RolePrincipal rolePrincipal) throws SecurityException {

        verifyRoleName(rolePrincipal.getName());
        
        if (!fUserManagement.roleExists(rolePrincipal.getName())) {
            fUserManagement.removeRole(rolePrincipal.getName());
        }

    }

    /**
     * @see org.apache.jetspeed.security.spi.RoleSecurityHandler#getRolePrincipals(java.lang.String)
     */
    public List getRolePrincipals(String filter) {
        List rolePrincipals = new ArrayList();
        
        String[] roleNames = this.fUserManagement.findRoles(filter);
        for (int i = 0; i < roleNames.length; i++) {
            rolePrincipals.add(new RolePrincipalImpl(roleNames[i]));
        }        
        
        return rolePrincipals;
    }

    private void verifyRoleName(String roleName) {
        if (StringUtils.isEmpty(roleName)) {
            throw new IllegalArgumentException("The role name cannot be null or empty.");
        }
    }
    
}
