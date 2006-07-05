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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.prefs.Preferences;

import org.apache.jetspeed.security.HierarchyResolver;
import org.apache.jetspeed.security.SecurityException;
import org.apache.jetspeed.security.impl.GeneralizationHierarchyResolver;
import org.apache.jetspeed.security.impl.GroupPrincipalImpl;
import org.apache.jetspeed.security.impl.RolePrincipalImpl;
import org.apache.jetspeed.security.impl.UserPrincipalImpl;
import org.apache.jetspeed.security.om.InternalGroupPrincipal;
import org.apache.jetspeed.security.om.InternalRolePrincipal;
import org.apache.jetspeed.security.om.InternalUserPrincipal;
import org.apache.jetspeed.security.om.impl.InternalUserPrincipalImpl;
import org.apache.jetspeed.security.spi.SecurityAccess;
import org.apache.jetspeed.security.spi.SecurityMappingHandler;

/**
 * @see org.apache.jetspeed.security.spi.SecurityMappingHandler
 * @author <a href="mailto:dlestrat@apache.org">David Le Strat </a>
 */
public class IngridSecurityMappingHandler implements SecurityMappingHandler
{

    /** The role hierarchy resolver. */
    HierarchyResolver roleHierarchyResolver = new GeneralizationHierarchyResolver();

    /** The group hierarchy resolver. */
    HierarchyResolver groupHierarchyResolver = new GeneralizationHierarchyResolver();

    /** Common queries. */
    private SecurityAccess commonQueries = null;

    /**
     * <p>
     * Constructor providing access to the common queries.
     * </p>
     */
    public IngridSecurityMappingHandler()
    {
        this.commonQueries = new SecurityAccessImpl();
    }

    /**
     * <p>
     * Constructor providing access to the common queries and hierarchy
     * resolvers.
     * </p>
     */
    public IngridSecurityMappingHandler(SecurityAccess commonQueries, HierarchyResolver roleHierarchyResolver,
            HierarchyResolver groupHierarchyResolver)
    {
        this.commonQueries = commonQueries;
        if (null != roleHierarchyResolver)
        {
            this.roleHierarchyResolver = roleHierarchyResolver;
        }
        if (null != groupHierarchyResolver)
        {
            this.groupHierarchyResolver = groupHierarchyResolver;
        }
    }

    /**
     * @return Returns the roleHierarchyResolver.
     */
    public HierarchyResolver getRoleHierarchyResolver()
    {
        return roleHierarchyResolver;
    }

    /**
     * @see org.apache.jetspeed.security.spi.SecurityMappingHandler#setRoleHierarchyResolver(org.apache.jetspeed.security.HierarchyResolver)
     */
    public void setRoleHierarchyResolver(HierarchyResolver roleHierarchyResolver)
    {
        this.roleHierarchyResolver = roleHierarchyResolver;
    }

    /**
     * @return Returns the groupHierarchyResolver.
     */
    public HierarchyResolver getGroupHierarchyResolver()
    {
        return groupHierarchyResolver;
    }

    /**
     * @see org.apache.jetspeed.security.spi.SecurityMappingHandler#setGroupHierarchyResolver(org.apache.jetspeed.security.HierarchyResolver)
     */
    public void setGroupHierarchyResolver(HierarchyResolver groupHierarchyResolver)
    {
        this.groupHierarchyResolver = groupHierarchyResolver;
    }

    /**
     * @see org.apache.jetspeed.security.spi.SecurityMappingHandler#getRolePrincipals(java.lang.String)
     */
    public Set getRolePrincipals(String username)
    {
        Set rolePrincipals = new HashSet();
        InternalUserPrincipal internalUser = commonQueries.getInternalUserPrincipal(username);
        if (null != internalUser)
        {
            Collection internalRoles = internalUser.getRolePrincipals();
            if (null != internalRoles)
            {
                Iterator internalRolesIter = internalRoles.iterator();
                while (internalRolesIter.hasNext())
                {
                    InternalRolePrincipal internalRole = (InternalRolePrincipal) internalRolesIter.next();
                    Preferences preferences = Preferences.userRoot().node(internalRole.getFullPath());
                    String[] fullPaths = roleHierarchyResolver.resolve(preferences);
                    for (int i = 0; i < fullPaths.length; i++)
                    {
                        Principal rolePrincipal = new RolePrincipalImpl(RolePrincipalImpl
                                .getPrincipalNameFromFullPath(fullPaths[i]));
                        if (!rolePrincipals.contains(rolePrincipal))
                        {
                            rolePrincipals.add(rolePrincipal);
                        }
                    }
                }
            }
        }
        return rolePrincipals;
    }

    /**
     * @see org.apache.jetspeed.security.spi.SecurityMappingHandler#setRolePrincipal(java.lang.String,
     *      java.lang.String)
     */
    public void setRolePrincipal(String username, String roleFullPathName) throws SecurityException
    {
        InternalUserPrincipal internalUser = commonQueries.getInternalUserPrincipal(username);
        boolean isMappingOnly = false;
        if (null == internalUser)
        {
            // This is a record for mapping only.
            isMappingOnly = true;
            internalUser = new InternalUserPrincipalImpl(UserPrincipalImpl.getFullPathFromPrincipalName(username));
        }
        Collection internalRoles = internalUser.getRolePrincipals();
        // This should not be null. Check for null should be made by the caller.
        InternalRolePrincipal internalRole = commonQueries.getInternalRolePrincipal(RolePrincipalImpl
                .getFullPathFromPrincipalName(roleFullPathName));
        // Check anyway.
        if (null == internalRole)
        {
            throw new SecurityException(SecurityException.ROLE_DOES_NOT_EXIST.create(roleFullPathName));
        }
        internalRoles.add(internalRole);
        internalUser.setRolePrincipals(internalRoles);
        commonQueries.setInternalUserPrincipal(internalUser, isMappingOnly);
    }

    /**
     * @see org.apache.jetspeed.security.spi.SecurityMappingHandler#removeRolePrincipal(java.lang.String,
     *      java.lang.String)
     */
    public void removeRolePrincipal(String username, String roleFullPathName) throws SecurityException
    {
        boolean isMappingOnly = false;
        // Check is the record is used for mapping only.
        InternalUserPrincipal internalUser = commonQueries.getInternalUserPrincipal(username, false);
        if (null == internalUser)
        {
            internalUser = commonQueries.getInternalUserPrincipal(username, true);
            isMappingOnly = true;
        }
        if (null != internalUser)
        {
            Collection internalRoles = internalUser.getRolePrincipals();
            // This should not be null. Check for null should be made by the caller.
            InternalRolePrincipal internalRole = commonQueries.getInternalRolePrincipal(RolePrincipalImpl
                    .getFullPathFromPrincipalName(roleFullPathName));
            // Check anyway.
            if (null == internalRole)
            {
                throw new SecurityException(SecurityException.ROLE_DOES_NOT_EXIST.create(roleFullPathName));
            }
            internalRoles.remove(internalRole);
            // Remove dead mapping records. I.e. No mapping is associated with the specific record.
            if (isMappingOnly && internalRoles.isEmpty() && internalUser.getGroupPrincipals().isEmpty()
                    && internalUser.getPermissions().isEmpty())
            {
                commonQueries.removeInternalUserPrincipal(internalUser);
            }
            else
            {
                internalUser.setRolePrincipals(internalRoles);
                commonQueries.setInternalUserPrincipal(internalUser, isMappingOnly);
            }
        }
        else
        {
            throw new SecurityException(SecurityException.USER_DOES_NOT_EXIST.create(username));
        }
    }

    /**
     * @see org.apache.jetspeed.security.spi.SecurityMappingHandler#getRolePrincipalsInGroup(java.lang.String)
     */
    public Set getRolePrincipalsInGroup(String groupFullPathName)
    {
        Set rolePrincipals = new HashSet();

        Preferences preferences = Preferences.userRoot().node(
                GroupPrincipalImpl.getFullPathFromPrincipalName(groupFullPathName));
        String[] fullPaths = groupHierarchyResolver.resolve(preferences);
        for (int i = 0; i < fullPaths.length; i++)
        {
            InternalGroupPrincipal internalGroup = commonQueries.getInternalGroupPrincipal(fullPaths[i]);
            if (null != internalGroup)
            {
                Collection internalRoles = internalGroup.getRolePrincipals();
                if (null != internalRoles)
                {
                    Iterator internalRolesIter = internalRoles.iterator();
                    while (internalRolesIter.hasNext())
                    {
                        InternalRolePrincipal internalRole = (InternalRolePrincipal) internalRolesIter.next();
                        Principal rolePrincipal = new RolePrincipalImpl(UserPrincipalImpl
                                .getPrincipalNameFromFullPath(internalRole.getFullPath()));
                        if (!rolePrincipals.contains(rolePrincipal))
                        {
                            rolePrincipals.add(rolePrincipal);
                        }
                    }
                }
            }
        }
        return rolePrincipals;
    }

    /**
     * @see org.apache.jetspeed.security.spi.SecurityMappingHandler#setRolePrincipalInGroup(java.lang.String,
     *      java.lang.String)
     */
    public void setRolePrincipalInGroup(String groupFullPathName, String roleFullPathName) throws SecurityException
    {
        InternalGroupPrincipal internalGroup = commonQueries.getInternalGroupPrincipal(GroupPrincipalImpl
                .getFullPathFromPrincipalName(groupFullPathName));
        if (null == internalGroup)
        {
            throw new SecurityException(SecurityException.GROUP_DOES_NOT_EXIST.create(groupFullPathName));
        }
        Collection internalRoles = internalGroup.getRolePrincipals();
        InternalRolePrincipal internalRole = commonQueries.getInternalRolePrincipal(RolePrincipalImpl
                .getFullPathFromPrincipalName(roleFullPathName));
        internalRoles.add(internalRole);
        internalGroup.setRolePrincipals(internalRoles);
        commonQueries.setInternalGroupPrincipal(internalGroup, false);
    }

    /**
     * @see org.apache.jetspeed.security.spi.SecurityMappingHandler#removeRolePrincipalInGroup(java.lang.String,
     *      java.lang.String)
     */
    public void removeRolePrincipalInGroup(String groupFullPathName, String roleFullPathName) throws SecurityException
    {
        InternalGroupPrincipal internalGroup = commonQueries.getInternalGroupPrincipal(GroupPrincipalImpl
                .getFullPathFromPrincipalName(groupFullPathName));
        if (null == internalGroup)
        {
            throw new SecurityException(SecurityException.GROUP_DOES_NOT_EXIST.create(internalGroup));
        }
        Collection internalRoles = internalGroup.getRolePrincipals();
        InternalRolePrincipal internalRole = commonQueries.getInternalRolePrincipal(RolePrincipalImpl
                .getFullPathFromPrincipalName(roleFullPathName));
        internalRoles.remove(internalRole);
        internalGroup.setRolePrincipals(internalRoles);
        commonQueries.setInternalGroupPrincipal(internalGroup, false);
    }

    /**
     * @see org.apache.jetspeed.security.spi.SecurityMappingHandler#getGroupPrincipals(java.lang.String)
     */
    public Set getGroupPrincipals(String username)
    {
        Set groupPrincipals = new HashSet();
        InternalUserPrincipal internalUser = commonQueries.getInternalUserPrincipal(username);
        if (null != internalUser)
        {
            Collection internalGroups = internalUser.getGroupPrincipals();
            if (null != internalGroups)
            {
                Iterator internalGroupsIter = internalGroups.iterator();
                while (internalGroupsIter.hasNext())
                {
                    InternalGroupPrincipal internalGroup = (InternalGroupPrincipal) internalGroupsIter.next();
                    Preferences preferences = Preferences.userRoot().node(internalGroup.getFullPath());
                    String[] fullPaths = groupHierarchyResolver.resolve(preferences);
                    for (int i = 0; i < fullPaths.length; i++)
                    {
                        groupPrincipals.add(new GroupPrincipalImpl(GroupPrincipalImpl
                                .getPrincipalNameFromFullPath(fullPaths[i])));
                    }
                }
            }
        }
        return groupPrincipals;
    }

    /**
     * @see org.apache.jetspeed.security.spi.SecurityMappingHandler#getGroupPrincipalsInRole(java.lang.String)
     */
    public Set getGroupPrincipalsInRole(String roleFullPathName)
    {
        Set groupPrincipals = new HashSet();

        Preferences preferences = Preferences.userRoot().node(
                RolePrincipalImpl.getFullPathFromPrincipalName(roleFullPathName));
        String[] fullPaths = roleHierarchyResolver.resolve(preferences);
        for (int i = 0; i < fullPaths.length; i++)
        {
            InternalRolePrincipal internalRole = commonQueries.getInternalRolePrincipal(fullPaths[i]);
            if (null != internalRole)
            {
                Collection internalGroups = internalRole.getGroupPrincipals();
                if (null != internalGroups)
                {
                    Iterator internalGroupsIter = internalGroups.iterator();
                    while (internalGroupsIter.hasNext())
                    {
                        InternalGroupPrincipal internalGroup = (InternalGroupPrincipal) internalGroupsIter.next();
                        Principal groupPrincipal = new GroupPrincipalImpl(GroupPrincipalImpl
                                .getPrincipalNameFromFullPath(internalGroup.getFullPath()));
                        if (!groupPrincipals.contains(groupPrincipal))
                        {
                            groupPrincipals.add(groupPrincipal);
                        }
                    }
                }
            }
        }
        return groupPrincipals;
    }

    /**
     * @see org.apache.jetspeed.security.spi.SecurityMappingHandler#getUserPrincipalsInRole(java.lang.String)
     */
    public Set getUserPrincipalsInRole(String roleFullPathName)
    {
        Set userPrincipals = new HashSet();

        Preferences preferences = Preferences.userRoot().node(
                RolePrincipalImpl.getFullPathFromPrincipalName(roleFullPathName));
        String[] fullPaths = roleHierarchyResolver.resolve(preferences);
        for (int i = 0; i < fullPaths.length; i++)
        {
            InternalRolePrincipal internalRole = commonQueries.getInternalRolePrincipal(fullPaths[i]);
            if (null != internalRole)
            {
                Collection internalUsers = internalRole.getUserPrincipals();
                if (null != internalUsers)
                {
                    Iterator internalUsersIter = internalUsers.iterator();
                    while (internalUsersIter.hasNext())
                    {
                        InternalUserPrincipal internalUser = (InternalUserPrincipal) internalUsersIter.next();
                        Principal userPrincipal = new UserPrincipalImpl(UserPrincipalImpl
                                .getPrincipalNameFromFullPath(internalUser.getFullPath()));
                        if (!userPrincipals.contains(userPrincipal))
                        {
                            userPrincipals.add(userPrincipal);
                        }
                    }
                }
            }
        }
        return userPrincipals;
    }

    /**
     * @see org.apache.jetspeed.security.spi.SecurityMappingHandler#getUserPrincipalsInGroup(java.lang.String)
     */
    public Set getUserPrincipalsInGroup(String groupFullPathName)
    {
        Set userPrincipals = new HashSet();

        Preferences preferences = Preferences.userRoot().node(
                GroupPrincipalImpl.getFullPathFromPrincipalName(groupFullPathName));
        String[] fullPaths = groupHierarchyResolver.resolve(preferences);
        for (int i = 0; i < fullPaths.length; i++)
        {
            InternalGroupPrincipal internalGroup = commonQueries.getInternalGroupPrincipal(fullPaths[i]);
            if (null != internalGroup)
            {
                Collection internalUsers = internalGroup.getUserPrincipals();
                if (null != internalUsers)
                {
                    Iterator internalUsersIter = internalUsers.iterator();
                    while (internalUsersIter.hasNext())
                    {
                        InternalUserPrincipal internalUser = (InternalUserPrincipal) internalUsersIter.next();
                        Principal userPrincipal = new UserPrincipalImpl(UserPrincipalImpl
                                .getPrincipalNameFromFullPath(internalUser.getFullPath()));
                        if (!userPrincipals.contains(userPrincipal))
                        {
                            userPrincipals.add(userPrincipal);
                        }
                    }
                }
            }
        }
        return userPrincipals;
    }

    /**
     * @see org.apache.jetspeed.security.spi.SecurityMappingHandler#setUserPrincipalInGroup(java.lang.String,
     *      java.lang.String)
     */
    public void setUserPrincipalInGroup(String username, String groupFullPathName) throws SecurityException
    {
        InternalUserPrincipal internalUser = commonQueries.getInternalUserPrincipal(username);
        boolean isMappingOnly = false;
        if (null == internalUser)
        {
            // This is a record for mapping only.
            isMappingOnly = true;
            internalUser = new InternalUserPrincipalImpl(UserPrincipalImpl.getFullPathFromPrincipalName(username));
        }
        Collection internalGroups = internalUser.getGroupPrincipals();
        // This should not be null. Check for null should be made by the caller.
        InternalGroupPrincipal internalGroup = commonQueries.getInternalGroupPrincipal(GroupPrincipalImpl
                .getFullPathFromPrincipalName(groupFullPathName));
        // Check anyway.
        if (null == internalGroup)
        {
            throw new SecurityException(SecurityException.GROUP_DOES_NOT_EXIST.create(groupFullPathName));
        }
        internalGroups.add(internalGroup);
        internalUser.setGroupPrincipals(internalGroups);
        commonQueries.setInternalUserPrincipal(internalUser, isMappingOnly);
    }

    /**
     * @see org.apache.jetspeed.security.spi.SecurityMappingHandler#removeUserPrincipalInGroup(java.lang.String,
     *      java.lang.String)
     */
    public void removeUserPrincipalInGroup(String username, String groupFullPathName) throws SecurityException
    {
        boolean isMappingOnly = false;
        // Check is the record is used for mapping only.
        InternalUserPrincipal internalUser = commonQueries.getInternalUserPrincipal(username, false);
        if (null == internalUser)
        {
            internalUser = commonQueries.getInternalUserPrincipal(username, true);
            isMappingOnly = true;
        }
        if (null != internalUser)
        {
            Collection internalGroups = internalUser.getGroupPrincipals();
            // This should not be null. Check for null should be made by the caller.
            InternalGroupPrincipal internalGroup = commonQueries.getInternalGroupPrincipal(GroupPrincipalImpl
                    .getFullPathFromPrincipalName(groupFullPathName));
            // Check anyway.
            if (null == internalGroup)
            {
                throw new SecurityException(SecurityException.GROUP_DOES_NOT_EXIST.create(groupFullPathName));
            }
            internalGroups.remove(internalGroup);
            // Remove dead mapping records. I.e. No mapping is associated with the specific record.
            if (isMappingOnly && internalGroups.isEmpty() && internalUser.getRolePrincipals().isEmpty()
                    && internalUser.getPermissions().isEmpty())
            {
                commonQueries.removeInternalUserPrincipal(internalUser);
            }
            else
            {
            internalUser.setGroupPrincipals(internalGroups);
            commonQueries.setInternalUserPrincipal(internalUser, isMappingOnly);
            }
        }
        else
        {
            throw new SecurityException(SecurityException.USER_DOES_NOT_EXIST.create(username));
        }
    }

    /**
     * @see org.apache.jetspeed.security.spi.SecurityMappingHandler#setUserPrincipalInRole(java.lang.String,
     *      java.lang.String)
     */
    public void setUserPrincipalInRole(String username, String roleFullPathName) throws SecurityException
    {
        InternalUserPrincipal internalUser = commonQueries.getInternalUserPrincipal(username);
        boolean isMappingOnly = false;
        if (null == internalUser)
        {
            // This is a record for mapping only.
            isMappingOnly = true;
            internalUser = new InternalUserPrincipalImpl(UserPrincipalImpl.getFullPathFromPrincipalName(username));
        }
        Collection internalRoles = internalUser.getRolePrincipals();
        // This should not be null. Check for null should be made by the caller.
        InternalRolePrincipal internalRole = commonQueries.getInternalRolePrincipal(RolePrincipalImpl
                .getFullPathFromPrincipalName(roleFullPathName));
        // Check anyway.
        if (null == internalRole)
        {
            throw new SecurityException(SecurityException.ROLE_DOES_NOT_EXIST.create(roleFullPathName));
        }
        internalRoles.add(internalRole);
        internalUser.setRolePrincipals(internalRoles);
        commonQueries.setInternalUserPrincipal(internalUser, isMappingOnly);
    }

    /**
     * @see org.apache.jetspeed.security.spi.SecurityMappingHandler#removeUserPrincipalInRole(java.lang.String,
     *      java.lang.String)
     */
    public void removeUserPrincipalInRole(String username, String roleFullPathName) throws SecurityException
    {
        boolean isMappingOnly = false;
        // Check is the record is used for mapping only.
        InternalUserPrincipal internalUser = commonQueries.getInternalUserPrincipal(username, false);
        if (null == internalUser)
        {
            internalUser = commonQueries.getInternalUserPrincipal(username, true);
            isMappingOnly = true;
        }
        if (null != internalUser)
        {
            Collection internalRoles = internalUser.getRolePrincipals();
            // This should not be null. Check for null should be made by the caller.
            InternalRolePrincipal internalRole = commonQueries.getInternalRolePrincipal(RolePrincipalImpl
                    .getFullPathFromPrincipalName(roleFullPathName));
            // Check anyway.
            if (null == internalRole)
            {
                throw new SecurityException(SecurityException.ROLE_DOES_NOT_EXIST.create(roleFullPathName));
            }
            internalRoles.remove(internalRole);
            // Remove dead mapping records. I.e. No mapping is associated with the specific record.
            if (isMappingOnly && internalRoles.isEmpty() && internalUser.getGroupPrincipals().isEmpty()
                    && internalUser.getPermissions().isEmpty())
            {
                commonQueries.removeInternalUserPrincipal(internalUser);
            }
            else
            {
                internalUser.setRolePrincipals(internalRoles);
                commonQueries.setInternalUserPrincipal(internalUser, isMappingOnly);
            }
        }
        else
        {
            throw new SecurityException(SecurityException.USER_DOES_NOT_EXIST.create(username));
        }
    }
    
}