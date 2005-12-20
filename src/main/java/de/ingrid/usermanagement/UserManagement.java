/*
 * Copyright (c) 1997-2005 by media style GmbH
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
                ArrayList roleArray = (ArrayList) groupHash.get(groupName);
                if (!roleArray.contains(roleName)) {
                    roleArray.add(roleName);
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

    private boolean isUserInGroup(final String userName, final String groupName) {
        boolean result = false;

        if (userExists(userName) && groupExists(groupName)) {
            if (this.fUser2Group.containsKey(userName)) {
                HashMap groupHash = (HashMap) this.fUser2Group.get(userName);
                if (groupHash.containsKey(groupName)) {
                    result = true;
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

        String storedPassword = (String) this.fUsers.get(userName);
        if (storedPassword.equals(password)) {
            result = true;
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
     * @param user
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
     * @param group
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
     * @param role
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

    /**
     * @param userName
     * @return The password to the user name.
     * @throws SecurityException
     */
    public synchronized String getPassword(String userName) throws SecurityException {
        String result = null;

        if (userExists(userName)) {
            result = (String) this.fUsers.get(userName);
        } else {
            throw new SecurityException(SecurityException.USER_DOES_NOT_EXIST.create(userName));
        }

        return result;
    }

    /**
     * @param filter
     *            A wildcard filter.
     * @return All user names that match the filter.
     */
    public synchronized String[] find(final String filter) {
        ArrayList result = new ArrayList();

        for (Iterator iter = this.fUsers.keySet().iterator(); iter.hasNext();) {
            String regex = wildcardToRegex(filter);
            String userName = (String) iter.next();
            if (userName.matches(regex)) {
                result.add(userName);
            }
        }

        return (String[]) result.toArray(new String[result.size()]);
    }

    private String wildcardToRegex(final String filter) {
        String result = filter;

        result = result.replaceAll("\\*", ".*");
        result = result.replace('?', '.');

        return result;
    }

    /**
     * @param userName
     * @return All groups a user is member for.
     * @throws SecurityException
     */
    public synchronized String[] getGroupsForUser(String userName) throws SecurityException {
        ArrayList result = new ArrayList();

        if (userExists(userName)) {
            if (this.fUser2Group.containsKey(userName)) {
                HashMap groups = (HashMap) this.fUser2Group.get(userName);
                for (Iterator iter = groups.keySet().iterator(); iter.hasNext();) {
                    String groupName = (String) iter.next();
                    result.add(groupName);
                }
            }
        } else {
            throw new SecurityException(SecurityException.USER_DOES_NOT_EXIST.create(userName));
        }

        return (String[]) result.toArray(new String[result.size()]);
    }

    /**
     * @param groupName
     * @return All users in a group.
     */
    public synchronized String[] getUsersForGroup(String groupName) {
        ArrayList result = new ArrayList();

        for (Iterator iter = this.fUsers.keySet().iterator(); iter.hasNext();) {
            String userName = (String) iter.next();
            if (isUserInGroup(userName, groupName)) {
                result.add(userName);
            }
        }

        return (String[]) result.toArray(new String[result.size()]);
    }

    /**
     * @param userName
     * @param groupName
     * @throws SecurityException
     */
    public synchronized void addUserToGroup(String userName, String groupName) throws SecurityException {
        if (userExists(userName)) {
            if (groupExists(groupName)) {
                if (!this.fUser2Group.containsKey(userName)) {
                    ArrayList roleArray = new ArrayList();
                    HashMap groupHash = new HashMap();
                    groupHash.put(groupName, roleArray);
                    this.fUser2Group.put(userName, groupHash);
                }
            } else {
                throw new SecurityException(SecurityException.GROUP_DOES_NOT_EXIST.create(groupName));
            }
        } else {
            throw new SecurityException(SecurityException.USER_DOES_NOT_EXIST.create(userName));
        }
    }
}
