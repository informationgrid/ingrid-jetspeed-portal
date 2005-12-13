/*
 * Copyright (c) 1997-2005 by media style GmbH
 * 
 * $Source: $
 */

package de.ingrid.usermanagement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.jetspeed.i18n.KeyedMessage;
import org.apache.jetspeed.security.SecurityException;

/**
 *  
 */
public class UserManagement {

    private HashMap fUsers = new HashMap();

    private ArrayList fRoles = new ArrayList();

    private ArrayList fGroups = new ArrayList();

    private HashMap fUser2Group = new HashMap();

    private String fId;

    /**
     * @param name
     * @param password
     */
    public synchronized void addUser(final String name, final String password) {
        if (!this.fUsers.containsKey(name)) {
            this.fUsers.put(name, password);
        }
    }

    /**
     * @param name
     * @return True if the user exists, false otherwise.
     */
    final public boolean userExists(final String name) {
        return this.fUsers.containsKey(name);
    }

    /**
     * @param name
     * @return True if the group exists, false otherwise.
     */
    public boolean groupExists(final String name) {
        return this.fGroups.contains(name);
    }

    /**
     * @param group
     */
    public synchronized void addGroup(String group) {
        if (!this.fGroups.contains(group)) {
            this.fGroups.add(group);
        }
    }

    /**
     * @param userName
     */
    public void removeUser(final String userName) {
        this.fUsers.remove(userName);
        this.fUser2Group.remove(userName);
    }

    /**
     * @param groupName
     */
    public synchronized void removeGroup(final String groupName) {
        for (Iterator iter = this.fUser2Group.keySet().iterator(); iter.hasNext();) {
            String userName = (String) iter.next();
            removeUserFromGroup(userName, groupName);
        }
        this.fGroups.remove(groupName);
    }

    /**
     * @param name
     * @return True if the role exists, false otherwise.
     */
    public boolean roleExists(final String name) {
        return this.fRoles.contains(name);
    }

    /**
     * @param role
     */
    public synchronized void addRole(final String role) {
        if (!this.fRoles.contains(role)) {
            this.fRoles.add(role);
        }
    }

    /**
     * @param roleName
     */
    public synchronized void removeRole(final String roleName) {
        for (Iterator userIter = this.fUser2Group.keySet().iterator(); userIter.hasNext();) {
            String userName = (String) userIter.next();
            HashMap groupHash = (HashMap) this.fUser2Group.get(userName);
            for (Iterator groupIter = groupHash.keySet().iterator(); groupIter.hasNext();) {
                String groupName = (String) groupIter.next();
                removeUserFromRole(userName, groupName, roleName);
            }
        }
        this.fRoles.remove(roleName);
    }

    /**
     * @param userName
     * @param groupName
     * @param roleName
     */
    public synchronized void removeUserFromRole(final String userName, final String groupName, final String roleName) {
        if (userExists(userName) && groupExists(groupName) && roleExists(roleName)) {
            if (this.fUser2Group.containsKey(userName)) {
                HashMap groupHash = (HashMap) this.fUser2Group.get(userName);
                if (groupHash.containsKey(groupName)) {
                    ArrayList roleArray = (ArrayList) groupHash.get(groupName);
                    if (roleArray.contains(roleName)) {
                        roleArray.remove(roleName);
                    }
                }
            }
        }
    }

    /**
     * Bind a user to a group with a specific role. A user can have more roles in one group.
     * 
     * @param userName
     * @param groupName
     * @param roleName
     */
    final public synchronized void addUserToGroup(final String userName, final String groupName, final String roleName) {
        if (userExists(userName) && groupExists(groupName) && roleExists(roleName)) {
            if (this.fUser2Group.containsKey(userName)) {
                HashMap groupHash = (HashMap) this.fUser2Group.get(userName);
                if (groupHash.containsKey(groupName)) {
                    ArrayList roleArray = (ArrayList) groupHash.get(groupName);
                    if (!roleArray.contains(roleName)) {
                        roleArray.add(roleName);
                    }
                } else {
                    ArrayList roleArray = new ArrayList();
                    roleArray.add(roleName);
                    groupHash.put(groupName, roleArray);
                }
            } else {
                ArrayList roleArray = new ArrayList();
                roleArray.add(roleName);
                HashMap groupHash = new HashMap();
                groupHash.put(groupName, roleArray);
                this.fUser2Group.put(userName, groupHash);
            }
        }
    }

    /**
     * @param userName
     * @param groupName
     * @param roleName
     * @return True if the user is in the group with the role, false otherwise.
     */
    public boolean isUserInGroup(final String userName, final String groupName, final String roleName) {
        boolean result = false;

        if (userExists(userName) && groupExists(groupName) && roleExists(roleName)) {
            if (this.fUser2Group.containsKey(userName)) {
                HashMap groupHash = (HashMap) this.fUser2Group.get(userName);
                if (groupHash.containsKey(groupName)) {
                    ArrayList roleArray = (ArrayList) groupHash.get(groupName);
                    if (roleArray.contains(roleName)) {
                        result = true;
                    }
                }
            }
        }

        return result;
    }

    /**
     * @param userName
     * @param groupName
     */
    public synchronized void removeUserFromGroup(final String userName, final String groupName) {
        if (userExists(userName) && groupExists(groupName)) {
            if (this.fUser2Group.containsKey(userName)) {
                HashMap groupHash = (HashMap) this.fUser2Group.get(userName);
                if (groupHash.containsKey(groupName)) {
                    groupHash.remove(groupName);
                }
            }
        }
    }

    /**
     * @param userName
     * @param password
     * @return True if the supplied password is identical with the stored.
     */
    public synchronized boolean authenticate(final String userName, final String password) {
        boolean result = false;

        if (userExists(userName)) {
            String storedPassword = (String) this.fUsers.get(userName);
            if (storedPassword.equals(password)) {
                result = true;
            }
        }

        return result;
    }

    /**
     * @return The Id for hibernate.
     */
    private String getId() {
        return this.fId;
    }

    /**
     * Is used by hibernate.
     * 
     * @param id
     */
    private void setId(final String id) {
        this.fId = id;
    }

    /**
     * @param users
     *  
     */
    private synchronized void setUsers(final Serializable users) {
        this.fUsers = (HashMap) users;
    }

    /**
     * @return The user hash map as serializable.
     */
    private synchronized Serializable getUsers() {
        return this.fUsers;
    }

    /**
     * @param groups
     */
    private synchronized void setGroups(Serializable groups) {
        this.fGroups = (ArrayList) groups;
    }

    /**
     * @return The group hash map as serializable.
     */
    private synchronized Serializable getGroups() {
        return this.fGroups;
    }

    /**
     * @param roles
     */
    private synchronized void setRoles(final Serializable roles) {
        this.fRoles = (ArrayList) roles;
    }

    /**
     * @return The role hash map as serializable.
     */
    private synchronized Serializable getRoles() {
        return this.fRoles;
    }

    /**
     * @param user2group
     */
    private synchronized void setUserToGroup(Serializable user2group) {
        this.fUser2Group = (HashMap) user2group;
    }

    /**
     * @return The user to group relation hash map as serializable.
     */
    final private synchronized Serializable getUserToGroup() {
        return this.fUser2Group;
    }

    /**
     * @param username
     * @param oldPassword
     * @param newPassword
     * @throws SecurityException
     */
    final public synchronized void setPassword(String username, String oldPassword, String newPassword)
            throws SecurityException {
        if (authenticate(username, oldPassword)) {
            this.fUsers.put(username, newPassword);
        } else {
            throw new SecurityException(new KeyedMessage("Cannot authenticate."));
        }
    }
}
