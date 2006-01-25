/*
 * Copyright (c) 1997-2006 by media style GmbH
 */

package de.ingrid.usermanagement;

/**
 */
public class UserGroupRoleRelation {

    private Group fGroup = null;

    private Role fRole = null;

    private Integer fId = new Integer(0);

    private User fUser = null;

    /**
     */
    public UserGroupRoleRelation() {
        // for cglib
    }

    /**
     * @param user
     * @param group
     * @param role
     */
    public UserGroupRoleRelation(User user, Group group, Role role) {
        this.fUser = user;
        this.fGroup = group;
        this.fRole = role;
    }

    /**
     * The current group.
     * 
     * @return A group object.
     */
    public Group getGroup() {
        return this.fGroup;
    }

    /**
     * @param group
     */
    public void setGroup(Group group) {
        this.fGroup = group;
    }

    /**
     * The current role.
     * 
     * @return A role object.
     */
    public Role getRole() {
        return this.fRole;
    }

    /**
     * @param role
     */
    public void setRole(Role role) {
        this.fRole = role;
    }

    /**
     * @return The ID of the relation.
     */
    public Integer getId() {
        return this.fId;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.fId = id;
    }

    /**
     * If two objects have the same group name and the same role name they are equal.
     * 
     * @param obj
     *            The object the current one is compared to.
     * @return True if both objects are equal otherwise false.
     */
    public boolean equals(UserGroupRoleRelation obj) {
        boolean groupEqual = false;
        boolean roleEqual = false;

        if ((null != this.fGroup) && (null != this.fRole)) {
            groupEqual = (this.fGroup.getName().equals(obj.getGroup().getName()));
            roleEqual = (this.fRole.getName().equals(obj.getRole().getName()));
        }

        return (roleEqual && groupEqual);
    }

    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof UserGroupRoleRelation) {
            result = equals((UserGroupRoleRelation) obj);
        }

        return result;
    }

    /**
     * The current user.
     * 
     * @return A user object.
     */
    public User getUser() {
        return this.fUser;
    }

    /**
     * @param user
     */
    public void setUser(User user) {
        this.fUser = user;
    }
}
