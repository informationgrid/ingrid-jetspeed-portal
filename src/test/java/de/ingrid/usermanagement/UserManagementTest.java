/*
 * Created on 06.12.2005
 */
package de.ingrid.usermanagement;

import junit.framework.TestCase;

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
    
}
