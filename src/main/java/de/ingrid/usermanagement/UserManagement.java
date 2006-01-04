/*
 * Copyright (c) 1997-2005 by media style GmbH
 */

package de.ingrid.usermanagement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.jetspeed.security.SecurityException;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

/**
 * 
 */
public class UserManagement {

    private HibernateManager fHibernateManager = HibernateManager.getInstance();

    /**
     * @param name
     * @param password
     */
    public synchronized void addUser(final String name, final String password) {
        if (!userExists(name)) {
            User user = new User(name, password);
            this.fHibernateManager.save(user);
        }
    }

    /**
     * @param name
     * @return True if the user exists, false otherwise.
     */
    final public boolean userExists(final String name) {
        boolean result = false;

        User user = getUser(name);
        if (null != user) {
            result = true;
        }

        return result;
    }

    /**
     * @param name
     * @return True if the group exists, false otherwise.
     */
    public boolean groupExists(final String name) {
        boolean result = false;

        Group group = getGroup(name);
        if (null != group) {
            result = true;
        }

        return result;
    }

    /**
     * @param name
     */
    public synchronized void addGroup(String name) {
        if (!groupExists(name)) {
            Group group = new Group(name);
            this.fHibernateManager.save(group);
        }
    }

    /**
     * @param userName
     */
    public void removeUser(final String userName) {
        List relations = this.fHibernateManager.loadAllData(UserGroupRoleRelation.class, 0);

        if (null != relations) {
            for (Iterator iterator = relations.iterator(); iterator.hasNext();) {
                UserGroupRoleRelation relation = (UserGroupRoleRelation) iterator.next();
                User user = relation.getUser();
                if (user.getName().equals(userName)) {
                    this.fHibernateManager.delete(relation);
                }
            }
        }

        if (userExists(userName)) {
            User user = getUser(userName);
            this.fHibernateManager.delete(user);
        }
    }

    private User getUser(String userName) {
        return (User) this.fHibernateManager.readObject(User.class, userName);
    }

    /**
     * @param groupName
     */
    public synchronized void removeGroup(final String groupName) {
        List relations = this.fHibernateManager.loadAllData(UserGroupRoleRelation.class, 0);
        
        if (null != relations) {
            for (Iterator iterator = relations.iterator(); iterator.hasNext();) {
                UserGroupRoleRelation relation = (UserGroupRoleRelation) iterator.next();
                Group group = relation.getGroup();
                if (group.getName().equals(groupName)) {
                    this.fHibernateManager.delete(relation);
                }
            }
        }

        if (groupExists(groupName)) {
            Group group = getGroup(groupName);
            this.fHibernateManager.delete(group);
        }
    }

    private Group getGroup(String groupName) {
        return (Group) this.fHibernateManager.readObject(Group.class, groupName);
    }

    /**
     * @param roleName
     * @return True if the role exists, false otherwise.
     */
    public boolean roleExists(final String roleName) {
        boolean result = false;

        Role role = getRole(roleName);
        if (null != role) {
            result = true;
        }

        return result;
    }

    /**
     * @param name
     */
    public synchronized void addRole(final String name) {
        if (!roleExists(name)) {
            Role role = new Role(name);
            this.fHibernateManager.save(role);
        }
    }

    /**
     * @param roleName
     */
    public synchronized void removeRole(final String roleName) {
        if (roleExists(roleName)) {
            List relations = this.fHibernateManager.loadAllData(UserGroupRoleRelation.class, 0);

            if (null != relations) {
                for (Iterator iterator = relations.iterator(); iterator.hasNext();) {
                    UserGroupRoleRelation relation = (UserGroupRoleRelation) iterator.next();
                    Role role = relation.getRole();
                    if (role.getName().equals(roleName)) {
                        this.fHibernateManager.delete(relation);
                    }
                }
            }

            Role role = getRole(roleName);
            this.fHibernateManager.delete(role);
        }
    }

    private Role getRole(String roleName) {
        return (Role) this.fHibernateManager.readObject(Role.class, roleName);
    }

    /**
     * @param userName
     * @param groupName
     * @param roleName
     */
    public synchronized void removeUserFromRole(final String userName, final String groupName, final String roleName) {
        if (userExists(userName) && groupExists(groupName) && roleExists(roleName)) {
            List relations = this.fHibernateManager.loadAllData(UserGroupRoleRelation.class, 0);

            User user = getUser(userName);
            Group group = getGroup(groupName);
            Role role = getRole(roleName);
            UserGroupRoleRelation relationToCompareWith = new UserGroupRoleRelation(user, group, role);
            for (Iterator iter = relations.iterator(); iter.hasNext();) {
                UserGroupRoleRelation relation = (UserGroupRoleRelation) iter.next();
                if (relation.equals(relationToCompareWith)) {
                    this.fHibernateManager.delete(relation);
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
    public synchronized void addUserToGroup(final String userName, final String groupName, final String roleName) {
        if (userExists(userName) && groupExists(groupName) && roleExists(roleName)) {
            Group group = getGroup(groupName);
            Role role = getRole(roleName);
            User user = getUser(userName);
            UserGroupRoleRelation newRelation = new UserGroupRoleRelation(user, group, role);
            // TODO: if new relation does not exist store
            this.fHibernateManager.save(newRelation);
            // endif
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
            List relations = this.fHibernateManager.loadAllData(UserGroupRoleRelation.class, 0);

            User user = getUser(userName);
            Group group = getGroup(groupName);
            Role role = getRole(roleName);
            final UserGroupRoleRelation relationToCompareWith = new UserGroupRoleRelation(user, group, role);

            for (Iterator iter = relations.iterator(); iter.hasNext();) {
                UserGroupRoleRelation element = (UserGroupRoleRelation) iter.next();
                if (relationToCompareWith.equals(element)) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * @param userName
     * @param groupName
     * @return True if a user is in the group otherwise false.
     */
    public synchronized boolean isUserInGroup(final String userName, final String groupName) {
        boolean result = false;

        if (userExists(userName) && groupExists(groupName)) {
            List relations = this.fHibernateManager.loadAllData(UserGroupRoleRelation.class, 0);
            for (Iterator iter = relations.iterator(); iter.hasNext();) {
                UserGroupRoleRelation element = (UserGroupRoleRelation) iter.next();
                User user = element.getUser();
                Group group = element.getGroup();
                String elementGroupName = group.getName();
                if ((userName.equals(user.getName())) && (null != elementGroupName)) {
                    if (elementGroupName.equals(groupName)) {
                        result = true;
                        break;
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
            User user = getUser(userName);
            List relations = this.fHibernateManager.loadAllData(UserGroupRoleRelation.class, 0);
            for (Iterator iter = relations.iterator(); iter.hasNext();) {
                UserGroupRoleRelation element = (UserGroupRoleRelation) iter.next();
                Group group = element.getGroup();
                String elementGroupName = group.getName();
                if ((null != elementGroupName) && elementGroupName.equals(groupName)) {
                    this.fHibernateManager.delete(element);
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
            User user = getUser(userName);
            String storedPassword = user.getPassword();
            if (storedPassword.equals(password)) {
                result = true;
            }
        }

        return result;
    }

    /**
     * @param userName
     * @param oldPassword
     * @param newPassword
     * @throws SecurityException
     */
    final public synchronized void setPassword(String userName, String oldPassword, String newPassword)
            throws SecurityException {
        if (authenticate(userName, oldPassword)) {
            User user = getUser(userName);
            user.setPassword(newPassword);
            this.fHibernateManager.update(user);
        } else {
            throw new SecurityException(SecurityException.INCORRECT_PASSWORD.create("Cannot authenticate."));
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
            User user = getUser(userName);
            result = user.getPassword();
        } else {
            throw new SecurityException(SecurityException.USER_DOES_NOT_EXIST.create(userName));
        }

        return result;
    }

    /**
     * @param filter
     *            A wildcard filter.
     * @return All user that match the filter.
     */
    public synchronized String[] find(final String filter) {
        ArrayList result = new ArrayList();
        List relations = this.fHibernateManager.loadAllData(User.class, 0);

        final String filterRegexp = wildcardToRegex(filter);
        if (null != relations) {
            for (Iterator iter = relations.iterator(); iter.hasNext();) {
                User user = (User) iter.next();
                final String userName = user.getName();
                if (userName.matches(filterRegexp)) {
                    result.add(userName);
                }
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
    public synchronized String[] getGroupsForUser(final String userName) throws SecurityException {
        ArrayList result = new ArrayList();

        if (userExists(userName)) {
            List relations = this.fHibernateManager.loadAllData(UserGroupRoleRelation.class, 0);
            for (Iterator iter = relations.iterator(); iter.hasNext();) {
                UserGroupRoleRelation relation = (UserGroupRoleRelation) iter.next();
                Group group = relation.getGroup();
                String groupName = group.getName();
                User user = relation.getUser();
                if ((userName.equals(user.getName())) && (null != groupName)) {
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

        List users = this.fHibernateManager.loadAllData(User.class, 0);

        if (null != users) {
            for (Iterator iter = users.iterator(); iter.hasNext();) {
                User user = (User) iter.next();
                final String userName = user.getName();
                if (isUserInGroup(userName, groupName)) {
                    result.add(userName);
                }
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
                if (!isUserInGroup(userName, groupName)) {
                    addRole("");
                    addUserToGroup(userName, groupName, "");
                }
            } else {
                throw new SecurityException(SecurityException.GROUP_DOES_NOT_EXIST.create(groupName));
            }
        } else {
            throw new SecurityException(SecurityException.USER_DOES_NOT_EXIST.create(userName));
        }
    }
}
