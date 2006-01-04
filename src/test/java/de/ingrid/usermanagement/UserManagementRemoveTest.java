/*
 * Copyright (c) 1997-2005 by media style GmbH
 * 
 * $Source: $
 */

package de.ingrid.usermanagement;

import junit.framework.TestCase;

/**
 * 
 */
public class UserManagementRemoveTest extends TestCase {

    private UserManagement fUserManagement = new UserManagement();

    private void beforeTest() {
        this.fUserManagement.addRole("role");
        this.fUserManagement.addGroup("group");
        this.fUserManagement.addUser("user", "password");
        this.fUserManagement.addUserToGroup("user", "group", "role");
    }

    private void afterTest() {
        this.fUserManagement.removeUser("user");
        this.fUserManagement.removeGroup("group");
        this.fUserManagement.removeRole("role");
    }

    /**
     * Tests if after a role deletion the relation is completly removed.
     */
    public void testCompleteRoleRemoval() {
        beforeTest();

        this.fUserManagement.removeRole("role");
        
        assertFalse(this.fUserManagement.isUserInGroup("user", "group", "role"));
        this.fUserManagement.addRole("role");
        assertFalse(this.fUserManagement.isUserInGroup("user", "group", "role"));

        afterTest();
    }

    /**
     * Tests if after a user deletion the relation is completly removed.
     */
    public void testCompleteUserRemoval() {
        beforeTest();

        this.fUserManagement.removeUser("user");
        assertFalse(this.fUserManagement.isUserInGroup("user", "group", "role"));
        this.fUserManagement.addUser("user", "passwd");
        assertFalse(this.fUserManagement.isUserInGroup("user", "group", "role"));

        afterTest();
    }

    /**
     * Tests if after a group deletion the relation is completly removed.
     */
    public void testCompleteGroupRemoval() {
        beforeTest();

        this.fUserManagement.removeGroup("group");
        assertFalse(this.fUserManagement.isUserInGroup("user", "group", "role"));
        this.fUserManagement.addGroup("group");
        assertFalse(this.fUserManagement.isUserInGroup("user", "group", "role"));

        afterTest();
    }
}
