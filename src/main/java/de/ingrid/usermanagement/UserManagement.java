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
    public void addUser(String name, String password) {
        if (!this.fUsers.containsKey(name)) {
            this.fUsers.put(name, password);
        }
    }

    /**
     * @param name
     * @return True if the user exists, false otherwise.
     */
    public boolean userExists(String name) {
        return this.fUsers.containsKey(name);
    }

    /**
     * @param name
     * @return True if the group exists, false otherwise.
     */
    public boolean groupExists(String name) {
        return this.fGroups.contains(name);
    }

    /**
     * @param group
     */
    public void addGroup(String group) {
        if (!this.fGroups.contains(group)) {
            this.fGroups.add(group);
        }
    }

    /**
     * @param userName
     */
    public void removeUser(String userName) {
        this.fUsers.remove(userName);
        this.fUser2Group.remove(userName);
    }

    /**
     * @param groupName
     */
    public void removeGroup(String groupName) {
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
    public boolean roleExists(String name) {
        return this.fRoles.contains(name);
    }

    /**
     * @param role
     */
    public synchronized void addRole(String role) {
        if (!this.fRoles.contains(role)) {
            this.fRoles.add(role);
        }
    }

    /**
     * @param roleName
     */
    public synchronized void removeRole(String roleName) {
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
    public synchronized void removeUserFromRole(String userName, String groupName, String roleName) {
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
    public void addUserToGroup(String userName, String groupName, String roleName) {
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
    public boolean isUserInGroup(String userName, String groupName, String roleName) {
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
    public void removeUserFromGroup(String userName, String groupName) {
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
    public boolean authenticate(String userName, String password) {
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
    public String getId() {
        return this.fId;
    }

    /**
     * Is used by hibernate.
     * 
     * @param id
     */
    private void setId(String id) {
        this.fId = id;
    }

    /**
     * @param users
     *  
     */
    public void setUsers(Serializable users) {
        this.fUsers = (HashMap) users;
    }

    /**
     * @return The user hash map as serializable.
     */
    public Serializable getUsers() {
        return this.fUsers;
    }

    /**
     * @param groups
     */
    public void setGroups(Serializable groups) {
        this.fGroups = (ArrayList) groups;
    }

    /**
     * @return The group hash map as serializable.
     */
    public Serializable getGroups() {
        return this.fGroups;
    }

    /**
     * @param roles
     */
    public void setRoles(Serializable roles) {
        this.fRoles = (ArrayList) roles;
    }

    /**
     * @return The role hash map as serializable.
     */
    public Serializable getRoles() {
        return this.fRoles;
    }

    /**
     * @param user2group
     */
    public void setUserToGroup(Serializable user2group) {
        this.fUser2Group = (HashMap) user2group;
    }

    /**
     * @return The user to group relation hash map as serializable.
     */
    public Serializable getUserToGroup() {
        return this.fUser2Group;
    }

    /**
     * @param username
     * @param oldPassword
     * @param newPassword
     * @throws SecurityException
     */
    public void setPassword(String username, String oldPassword, String newPassword) throws SecurityException {
        if (authenticate(username, oldPassword)) {
            this.fUsers.put(username, newPassword);
        } else {
            throw new SecurityException(new KeyedMessage("Cannot authenticate."));
        }
    }
}
