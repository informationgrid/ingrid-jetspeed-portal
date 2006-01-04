/*
 * Copyright (c) 1997-2005 by media style GmbH
 */

package de.ingrid.usermanagement;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * 
 */
public class UserManagementPersistenceTest extends TestCase {

    private UserManagement fUserManagement;

    protected void setUp() {
        this.fUserManagement = new UserManagement();
    }

    protected void tearDown() {
        this.fUserManagement = null;
    }

    /**
     * 
     */
    public void testPersistenceWrite() {
        this.fUserManagement.removeUser("user");
        this.fUserManagement.addGroup("group");
        this.fUserManagement.addRole("role");
        this.fUserManagement.addUser("user", "password");
        this.fUserManagement.addUserToGroup("user", "group", "role");
    }

    /**
     * 
     */
    public void testPersistenceRead() {
        boolean auth = false;
        auth = this.fUserManagement.authenticate("user", "password");
        assertTrue(auth);
    }

    /**
     * 
     */
    public void testPersistenceUpdateWrite() {
        this.fUserManagement.removeUser("user2");
        this.fUserManagement.addUser("user2", "password2");

        assertNotNull(this.fUserManagement);
    }

    /**
     * 
     */
    public void testPersistenceUpdateRead() {
        boolean auth = false;
        auth = (this.fUserManagement.authenticate("user", "password") && this.fUserManagement.authenticate("user2",
                "password2"));
        assertTrue(auth);
        
        this.fUserManagement.removeUser("user");
        this.fUserManagement.removeUser("user2");
    }
}
