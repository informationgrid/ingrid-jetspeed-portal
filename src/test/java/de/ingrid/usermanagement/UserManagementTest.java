/*
 * Copyright (c) 1997-2005 by media style GmbH
 */

package de.ingrid.usermanagement;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.jetspeed.security.SecurityException;

/**
 * 
 */
public class UserManagementTest extends TestCase {

    private UserManagement fUserManagement;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        this.fUserManagement = new UserManagement();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        this.fUserManagement = null;

        super.tearDown();
    }

    /**
     * 
     */
    public void testAddUser() {
        this.fUserManagement.addUser("user1", "pwd");
        assertTrue(this.fUserManagement.userExists("user1"));
        this.fUserManagement.removeUser("user1");
    }

    /**
     * 
     */
    public void testAddGroup() {
        this.fUserManagement.addGroup("group1");
        assertTrue(this.fUserManagement.groupExists("group1"));
        this.fUserManagement.removeGroup("group1");
    }

    /**
     * 
     */
    public void testAddRole() {
        this.fUserManagement.addRole("role1");
        assertTrue(this.fUserManagement.roleExists("role1"));
        this.fUserManagement.removeRole("role1");
    }

    /**
     * 
     */
    public void testRemoveGroup() {
        this.fUserManagement.addGroup("group1");

        this.fUserManagement.removeGroup("group1");
        assertFalse(this.fUserManagement.groupExists("group1"));
    }

    /**
     * 
     */
    public void testRemoveUser() {
        this.fUserManagement.addUser("user1", "pwd");

        this.fUserManagement.removeUser("user1");
        assertFalse(this.fUserManagement.userExists("user1"));
    }

    /**
     * 
     */
    public void testRemoveRole() {
        this.fUserManagement.addRole("role1");

        this.fUserManagement.removeRole("role1");
        assertFalse(this.fUserManagement.roleExists("role1"));
    }

    /**
     * 
     */
    public void testAddUserToGroup() {
        this.fUserManagement.addUser("user1", "pwd");
        this.fUserManagement.addGroup("group1");
        this.fUserManagement.addRole("role1");

        this.fUserManagement.addUserToGroup("user1", "group1", "role1");
        assertTrue(this.fUserManagement.isUserInGroup("user1", "group1", "role1"));
        
        this.fUserManagement.removeUserFromGroup("user1", "group1");
        this.fUserManagement.removeGroup("group1");
        this.fUserManagement.removeRole("role1");
        this.fUserManagement.removeUser("user1");
    }

    /**
     */
    public void testAddUserToGroupTo2Groups() {
        this.fUserManagement.addUser("user", "pwd");
        this.fUserManagement.addGroup("group");
        this.fUserManagement.addGroup("group1");
        this.fUserManagement.addRole("role");

        this.fUserManagement.addUserToGroup("user", "group", "role");
        this.fUserManagement.addUserToGroup("user", "group1", "role");
        assertTrue(this.fUserManagement.isUserInGroup("user", "group", "role"));
        assertTrue(this.fUserManagement.isUserInGroup("user", "group1", "role"));
    }

    /**
     * @throws SecurityException
     */
    public void testAddUserToGroup2Params() throws SecurityException {
        this.fUserManagement.addUser("user", "pwd");
        this.fUserManagement.addGroup("group");

        this.fUserManagement.addUserToGroup("user", "group");
        assertTrue(this.fUserManagement.isUserInGroup("user", "group"));
    }

    /**
     * @throws SecurityException
     */
    public void testAddUserToGroup2ParamsTo2Groups() throws SecurityException {
        this.fUserManagement.addUser("user", "pwd");
        this.fUserManagement.addGroup("group");
        this.fUserManagement.addGroup("group1");

        this.fUserManagement.addUserToGroup("user", "group");
        this.fUserManagement.addUserToGroup("user", "group1");
        assertTrue(this.fUserManagement.isUserInGroup("user", "group"));
        assertTrue(this.fUserManagement.isUserInGroup("user", "group1"));
    }

    /**
     * 
     */
    public void testRemoveUserFromGroup() {
        this.fUserManagement.addUser("user1", "pwd");
        this.fUserManagement.addGroup("group1");
        this.fUserManagement.addRole("role1");
        this.fUserManagement.addUserToGroup("user1", "group1", "role1");

        this.fUserManagement.removeUserFromGroup("user1", "group1");
        assertFalse(this.fUserManagement.isUserInGroup("user1", "group1", "role1"));
    }

    /**
     * 
     */
    public void testRemoveUserFromRole() {
        this.fUserManagement.addUser("user1", "pwd");
        this.fUserManagement.addGroup("group1");
        this.fUserManagement.addRole("role1");
        this.fUserManagement.addRole("role2");
        this.fUserManagement.addUserToGroup("user1", "group1", "role1");
        this.fUserManagement.addUserToGroup("user1", "group1", "role2");

        this.fUserManagement.removeUserFromRole("user1", "group1", "role1");
        assertFalse(this.fUserManagement.isUserInGroup("user1", "group1", "role1"));
        assertTrue(this.fUserManagement.isUserInGroup("user1", "group1", "role2"));
    }

    /**
     * 
     */
    public void testAuthenticate() {
        this.fUserManagement.addUser("user1", "pwd");

        assertTrue(this.fUserManagement.authenticate("user1", "pwd"));
    }

    /**
     * 
     */
    public void testFalseAuthenticate() {
        this.fUserManagement.addUser("user1", "pwd");

        assertFalse(this.fUserManagement.authenticate("user1", ""));
        assertFalse(this.fUserManagement.authenticate("user1", null));
        assertFalse(this.fUserManagement.authenticate("user1", "pwdd"));
        assertFalse(this.fUserManagement.authenticate("user1", "ppwd"));
        assertFalse(this.fUserManagement.authenticate("user1", "ppwdd"));
    }

    /**
     * @throws SecurityException
     * 
     */
    public void testSetPassword() throws SecurityException {
        this.fUserManagement.removeUser("user12");
        this.fUserManagement.addUser("user12", "pwd");
        this.fUserManagement.setPassword("user12", "pwd", "dwp");

        assertTrue(this.fUserManagement.authenticate("user12", "dwp"));
        this.fUserManagement.removeUser("user12");
    }

    /**
     */
    public void testCannotSetPassword() {
        this.fUserManagement.removeUser("user9867589");
        this.fUserManagement.addUser("user9867589", "dwp");
        try {
            this.fUserManagement.setPassword("user9867589", "pwd", "dwp");
            fail();
        } catch (SecurityException e) {
            assertTrue(this.fUserManagement.authenticate("user9867589", "dwp"));
        }
        this.fUserManagement.removeUser("user9867589");
    }

    /**
     * @throws SecurityException
     */
    public void testGetPassword() throws SecurityException {
        this.fUserManagement.removeUser("user4867439");
        this.fUserManagement.addUser("user4867439", "dwp");
        final String password = this.fUserManagement.getPassword("user4867439");
        assertEquals("dwp", password);
        this.fUserManagement.removeUser("user4867439");
    }

    /**
     * 
     */
    public void testGetPasswordNoUser() {
        try {
            this.fUserManagement.getPassword("user5");
            fail();
        } catch (SecurityException e) {
            assertEquals(SecurityException.USER_DOES_NOT_EXIST.getKey(), e.getKeyedMessage().getKey());
        }
    }

    /**
     * @throws SecurityException
     * 
     */
    public void testGetGroupsForUser() throws SecurityException {
        this.fUserManagement.removeUser("user5645654");
        this.fUserManagement.removeUser("user56456547");
        this.fUserManagement.addUser("user5645654", "pwd");
        this.fUserManagement.addUser("user56456547", "pwd");
        this.fUserManagement.addGroup("group1");
        this.fUserManagement.addGroup("group2");
        this.fUserManagement.addGroup("group3");
        this.fUserManagement.addGroup("group4");
        this.fUserManagement.addUserToGroup("user5645654", "group1");
        this.fUserManagement.addUserToGroup("user5645654", "group2");
        this.fUserManagement.addUserToGroup("user5645654", "group3");
        this.fUserManagement.addUserToGroup("user56456547", "group4");

        String[] groups = this.fUserManagement.getGroupsForUser("user56456547");
        assertEquals(1, groups.length);
        assertEquals("group4", groups[0]);

        groups = this.fUserManagement.getGroupsForUser("user5645654");
        assertEquals(3, groups.length);

        this.fUserManagement.removeUser("user5645654");
        this.fUserManagement.removeUser("user56456547");
    }

    /**
     */
    public void testGetGroupsForUserNoUser() {
        try {
            this.fUserManagement.getGroupsForUser("user90");
            fail();
        } catch (SecurityException e) {
            assertEquals(SecurityException.USER_DOES_NOT_EXIST.getKey(), e.getKeyedMessage().getKey());
        }
    }

    /**
     */
    public void testAddUserToGroupNoUser() {
        try {
            this.fUserManagement.addUserToGroup("user90", "group");
            fail();
        } catch (SecurityException e) {
            assertEquals(SecurityException.USER_DOES_NOT_EXIST.getKey(), e.getKeyedMessage().getKey());
        }
    }

    /**
     */
    public void testAddUserToGroupNoGroup() {
        try {
            this.fUserManagement.addUserToGroup("user", "group90");
            fail();
        } catch (SecurityException e) {
            assertEquals(SecurityException.GROUP_DOES_NOT_EXIST.getKey(), e.getKeyedMessage().getKey());
        }
    }

    /**
     * @throws SecurityException
     */
    public void testGetUsersForGroup() throws SecurityException {
        this.fUserManagement.addUser("user1", "pwd");
        this.fUserManagement.addUser("user2", "pwd");
        this.fUserManagement.addUser("user3", "pwd");
        this.fUserManagement.addUser("user4", "pwd");
        this.fUserManagement.addGroup("group1");
        this.fUserManagement.addGroup("group2");
        this.fUserManagement.addUserToGroup("user1", "group1");
        this.fUserManagement.addUserToGroup("user2", "group1");
        this.fUserManagement.addUserToGroup("user3", "group1");
        this.fUserManagement.addUserToGroup("user4", "group2");

        String[] groups = this.fUserManagement.getUsersForGroup("group2");
        assertEquals(1, groups.length);
        assertEquals("user4", groups[0]);

        groups = this.fUserManagement.getUsersForGroup("group1");
        assertEquals(3, groups.length);
    }
}
