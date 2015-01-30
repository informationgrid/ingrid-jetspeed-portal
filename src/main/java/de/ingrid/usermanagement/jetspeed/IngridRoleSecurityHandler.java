/*
 * **************************************************-
 * InGrid Portal Security Provider
 * ==================================================
 * Copyright (C) 2014 - 2015 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.1 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
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
     * Clean up Objects resources. Close the DB Connection. 
     */
    public void destroy() {
        ((SecurityAccessImpl)securityAccess).destroy();
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
