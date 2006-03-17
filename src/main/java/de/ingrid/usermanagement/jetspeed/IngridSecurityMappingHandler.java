/*
 * Copyright (c) 1997-2005 by media style GmbH
 */

package de.ingrid.usermanagement.jetspeed;

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.prefs.Preferences;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jetspeed.security.GroupPrincipal;
import org.apache.jetspeed.security.HierarchyResolver;
import org.apache.jetspeed.security.RolePrincipal;
import org.apache.jetspeed.security.SecurityException;
import org.apache.jetspeed.security.impl.GeneralizationHierarchyResolver;
import org.apache.jetspeed.security.impl.GroupPrincipalImpl;
import org.apache.jetspeed.security.impl.RolePrincipalImpl;
import org.apache.jetspeed.security.impl.UserPrincipalImpl;
import org.apache.jetspeed.security.om.InternalGroupPrincipal;
import org.apache.jetspeed.security.om.InternalRolePrincipal;
import org.apache.jetspeed.security.spi.SecurityMappingHandler;

import de.ingrid.usermanagement.UserManagement;

/**
 * 
 */
public class IngridSecurityMappingHandler implements SecurityMappingHandler {

    private final Log fLogger = LogFactory.getLog(this.getClass());

    /** The role hierarchy resolver. */
    private HierarchyResolver fRoleHierarchyResolver = new GeneralizationHierarchyResolver();

    /** The group hierarchy resolver. */
    private HierarchyResolver fGroupHierarchyResolver = new GeneralizationHierarchyResolver();

    private UserManagement fUserManagement = new UserManagement();

    public HierarchyResolver getRoleHierarchyResolver() {
        return this.fRoleHierarchyResolver;
    }

    public void setRoleHierarchyResolver(HierarchyResolver roleHierarchyResolver) {
        this.fRoleHierarchyResolver = roleHierarchyResolver;
    }

    public HierarchyResolver getGroupHierarchyResolver() {
        return this.fGroupHierarchyResolver;
    }

    public void setGroupHierarchyResolver(HierarchyResolver groupHierarchyResolver) {
        this.fGroupHierarchyResolver = groupHierarchyResolver;
    }

    public Set getRolePrincipals(String username) {
        Set rolePrincipals = new HashSet();

        try {
            String[] roles = this.fUserManagement.getRolesForUser(username);
            for (int i = 0; i < roles.length; i++) {
                createResolvedRolePrincipalSet(username, rolePrincipals, roles, i);
            }
        } catch (SecurityException e) {
            this.fLogger.warn(e);
        }

        return rolePrincipals;
    }

    public void setRolePrincipal(String username, String roleFullPathName) throws SecurityException {
        // Not yet implemented.
    }

    public void removeRolePrincipal(String username, String roleFullPathName) throws SecurityException {
        // Not yet implemented.
    }

    public Set getRolePrincipalsInGroup(String groupFullPathName) {
        Set rolePrincipals = new HashSet();

        Preferences preferences = Preferences.userRoot().node(
                GroupPrincipalImpl.getFullPathFromPrincipalName(groupFullPathName));
        String[] fullPaths = this.fGroupHierarchyResolver.resolve(preferences);
        for (int i = 0; i < fullPaths.length; i++)
        {
            String[] roles = this.fUserManagement.getRolesForGroup(fullPaths[i]);
            
            for (int j = 0; j < roles.length; j++) {
                createResolvedRolePrincipalSet("not specified", rolePrincipals, roles, j);
            }
        }
        return rolePrincipals;        
    }

    public void setRolePrincipalInGroup(String groupFullPathName, String roleFullPathName) throws SecurityException {
        // Not yet implemented.
    }

    public void removeRolePrincipalInGroup(String groupFullPathName, String roleFullPathName) throws SecurityException {
        // Not yet implemented.
    }

    public Set getGroupPrincipals(String userName) {
        Set groupPrincipals = new HashSet();

        try {
            String[] groups = this.fUserManagement.getGroupsForUser(userName);
            for (int i = 0; i < groups.length; i++) {
                createResolvedGroupPrincipalSet(userName, groupPrincipals, groups, i);
            }
        } catch (SecurityException e) {
            this.fLogger.warn(e);
        }

        return groupPrincipals;
    }

    public Set getGroupPrincipalsInRole(String roleFullPathName) {
        Set groupPrincipals = new HashSet();

        Preferences preferences = Preferences.userRoot().node(
                RolePrincipalImpl.getFullPathFromPrincipalName(roleFullPathName));
        String[] fullPaths = this.fRoleHierarchyResolver.resolve(preferences);
        for (int i = 0; i < fullPaths.length; i++)
        {
            String[] groups = this.fUserManagement.getGroupsForRole(fullPaths[i]);
            
            for (int j = 0; j < groups.length; j++) {
                createResolvedRolePrincipalSet("not specified", groupPrincipals, groups, j);
            }
        }
        return groupPrincipals;        
    }

    public Set getUserPrincipalsInRole(String roleFullPathName) {

        Set userPrincipals = new HashSet();

        Preferences preferences = Preferences.userRoot().node(
                RolePrincipalImpl.getFullPathFromPrincipalName(roleFullPathName));
        String[] fullPaths = this.fRoleHierarchyResolver.resolve(preferences);
        for (int i = 0; i < fullPaths.length; i++) {
            String[] usersInRole = this.fUserManagement.getUsersForRole(fullPaths[i]);
            for (int y = 0; y < usersInRole.length; y++) {
                Principal userPrincipal = new UserPrincipalImpl(usersInRole[y]);
                userPrincipals.add(userPrincipal);
            }
        }
        
        return userPrincipals;
    }

    public Set getUserPrincipalsInGroup(String groupFullPathName) {
        Set userPrincipals = new HashSet();

        Preferences preferences = Preferences.userRoot().node(groupFullPathName);
        String[] fullPaths = this.fGroupHierarchyResolver.resolve(preferences);

        getUserPrincipalsInGroup(userPrincipals, fullPaths);

        return userPrincipals;
    }

    public void setUserPrincipalInGroup(String username, String groupFullPathName) throws SecurityException {
        this.fUserManagement.addUserToGroup(username, groupFullPathName);
    }

    public void removeUserPrincipalInGroup(String userName, String groupFullPathName) throws SecurityException {
        this.fUserManagement.removeUserFromGroup(userName, groupFullPathName);
    }

    /**
     * @param username
     * @param groupPrincipals
     * @param groups
     * @param i
     */
    private void createResolvedGroupPrincipalSet(String username, Set groupPrincipals, String[] groups, int i) {
        this.fLogger.debug("Group [" + i + "] for user[" + username + "] is [" + groups[i] + "]");

        GroupPrincipal group = new GroupPrincipalImpl(groups[i]);
        Preferences preferences = Preferences.userRoot().node(group.getFullPath());
        this.fLogger.debug("Group name:" + group.getName());
        String[] fullPaths = this.fGroupHierarchyResolver.resolve(preferences);
        for (int n = 0; n < fullPaths.length; n++) {
            this.fLogger.debug("Group [" + i + "] for user[" + username + "] is ["
                    + GroupPrincipalImpl.getPrincipalNameFromFullPath(fullPaths[n]) + "]");
            groupPrincipals.add(new GroupPrincipalImpl(GroupPrincipalImpl.getPrincipalNameFromFullPath(fullPaths[n])));
        }
    }

    private void createResolvedRolePrincipalSet(String username, Set rolePrincipals, String[] roles, int i) {
        this.fLogger.debug("Role [" + i + "] for user[" + username + "] is [" + roles[i] + "]");

        RolePrincipal role = new RolePrincipalImpl(roles[i]);
        Preferences preferences = Preferences.userRoot().node(role.getFullPath());
        this.fLogger.debug("Role name:" + role.getName());
        String[] fullPaths = this.fRoleHierarchyResolver.resolve(preferences);
        for (int n = 0; n < fullPaths.length; n++) {
            this.fLogger.debug("Role [" + i + "] for user[" + username + "] is ["
                    + RolePrincipalImpl.getPrincipalNameFromFullPath(fullPaths[n]) + "]");
            rolePrincipals.add(new RolePrincipalImpl(RolePrincipalImpl.getPrincipalNameFromFullPath(fullPaths[n])));
        }
    }
    
    /**
     * Gets the user principals in groups.
     * 
     * @param userPrincipals
     * @param fullPaths
     */
    private void getUserPrincipalsInGroup(Set userPrincipals, String[] fullPaths) {
        for (int i = 0; i < fullPaths.length; i++) {
            String[] usersInGroup = this.fUserManagement.getUsersForGroup(fullPaths[i]);
            for (int y = 0; y < usersInGroup.length; y++) {
                Principal userPrincipal = new UserPrincipalImpl(usersInGroup[y]);
                userPrincipals.add(userPrincipal);
            }
        }
    }
}
