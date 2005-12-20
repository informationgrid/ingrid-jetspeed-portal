/*
 * Copyright (c) 1997-2005 by media style GmbH
 */

package de.ingrid.usermanagement;

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
    }

    /**
     *
     */
    public void testAddGroup() {
        this.fUserManagement.addGroup("group1");
        assertTrue(this.fUserManagement.groupExists("group1"));
    }

    /**
     *
     */
    public void testAddRole() {
        this.fUserManagement.addRole("role1");
        assertTrue(this.fUserManagement.roleExists("role1"));
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
        this.fUserManagement.addUser("user", "pwd");
        this.fUserManagement.setPassword("user", "pwd", "dwp");

        assertTrue(this.fUserManagement.authenticate("user", "dwp"));
    }

    /**
     */
    public void testCannotSetPassword() {
        this.fUserManagement.addUser("user", "pwd");

        try {
            this.fUserManagement.setPassword("user", "dwp", "pwd");
            fail();
        } catch (SecurityException e) {
            assertTrue(this.fUserManagement.authenticate("user", "pwd"));
        }
    }

    /**
     * @throws SecurityException
     */
    public void testGetPassword() throws SecurityException {
        this.fUserManagement.addUser("user", "pwd");

        final String password = this.fUserManagement.getPassword("user");
        assertEquals("pwd", password);
    }

    /**
     *
     */
    public void testGetPasswordNoUser() {
        try {
            this.fUserManagement.getPassword("user");
            fail();
        } catch (SecurityException e) {
            assertEquals(SecurityException.USER_DOES_NOT_EXIST.getKey(), e.getKeyedMessage().getKey());
        }
    }
}
