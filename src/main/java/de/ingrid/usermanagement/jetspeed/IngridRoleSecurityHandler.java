/*
 * Copyright (c) 2006 wemove digital solutions. All rights reserved.
 */
package de.ingrid.usermanagement.jetspeed;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.jetspeed.security.RolePrincipal;
import org.apache.jetspeed.security.SecurityException;
import org.apache.jetspeed.security.impl.RolePrincipalImpl;
import org.apache.jetspeed.security.om.InternalRolePrincipal;
import org.apache.jetspeed.security.om.impl.InternalRolePrincipalImpl;
import org.apache.jetspeed.security.spi.RoleSecurityHandler;
import org.apache.jetspeed.security.spi.SecurityAccess;

/**
 * Implements the role SecurityHandler Interface. 
 * Replacement for jetspeed's DefaultRoleSecurityHandler.
 *
 * @author joachim@wemove.com
 */
public class IngridRoleSecurityHandler implements RoleSecurityHandler {

    /** SecurityAccess. */
    private SecurityAccess securityAccess = null;

    /**
     * <p>Constructor providing access to the SecurityAccess implementation.</p>
     */
    public IngridRoleSecurityHandler()
    {
        this.securityAccess = new SecurityAccessImpl();
    }
    
    /**
     * @see org.apache.jetspeed.security.spi.RoleSecurityHandler#getRolePrincipal(java.lang.String)
     */
    public RolePrincipal getRolePrincipal(String roleFullPathName) {
        RolePrincipal rolePrincipal = null;
        InternalRolePrincipal internalRole = securityAccess
                .getInternalRolePrincipal(RolePrincipalImpl
                        .getFullPathFromPrincipalName(roleFullPathName));
        if (null != internalRole)
        {
            rolePrincipal = new RolePrincipalImpl(RolePrincipalImpl
                    .getPrincipalNameFromFullPath(internalRole.getFullPath()));
        }
        return rolePrincipal;
    }

    /**
     * @see org.apache.jetspeed.security.spi.RoleSecurityHandler#setRolePrincipal(org.apache.jetspeed.security.RolePrincipal)
     */
    public void setRolePrincipal(RolePrincipal rolePrincipal)
            throws SecurityException
    {
        String fullPath = rolePrincipal.getFullPath();
        InternalRolePrincipal internalRole = securityAccess.getInternalRolePrincipal(fullPath);
        if ( null == internalRole )
        {
            internalRole = new InternalRolePrincipalImpl(fullPath);
            internalRole.setEnabled(rolePrincipal.isEnabled());
            securityAccess.setInternalRolePrincipal(internalRole, false);
        }
        else if ( !internalRole.isMappingOnly() )
        {
            if ( internalRole.isEnabled() != rolePrincipal.isEnabled() )
            {
                internalRole.setEnabled(rolePrincipal.isEnabled());
                securityAccess.setInternalRolePrincipal(internalRole, false);
            }
        }
        else
        {
            // TODO: should we throw an exception here?
        }
    }

    /**
     * @see org.apache.jetspeed.security.spi.RoleSecurityHandler#removeRolePrincipal(org.apache.jetspeed.security.RolePrincipal)
     */
    public void removeRolePrincipal(RolePrincipal rolePrincipal)
            throws SecurityException
    {
        InternalRolePrincipal internalRole = securityAccess
                .getInternalRolePrincipal(rolePrincipal.getFullPath());
        if (null != internalRole)
        {
            securityAccess.removeInternalRolePrincipal(internalRole);
        }
    }

    /**
     * @see org.apache.jetspeed.security.spi.RoleSecurityHandler#getRolePrincipals(java.lang.String)
     */
    public List getRolePrincipals(String filter)
    {
        List rolePrincipals = new LinkedList();
        Iterator result = securityAccess.getInternalRolePrincipals(filter);
        while (result.hasNext())
        {
            InternalRolePrincipal internalRole = (InternalRolePrincipal) result
                    .next();
            String path = internalRole.getFullPath();
            if (path == null)
            {
                continue;
            }
            rolePrincipals.add(new RolePrincipalImpl(RolePrincipalImpl
                    .getPrincipalNameFromFullPath(internalRole.getFullPath())));
        }
        return rolePrincipals;
    }
 }
