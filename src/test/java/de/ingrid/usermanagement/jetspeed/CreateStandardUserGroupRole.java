/*
 * Copyright (c) 1997-2006 by media style GmbH
 */

package de.ingrid.usermanagement.jetspeed;

import de.ingrid.usermanagement.HibernateManager;
import de.ingrid.usermanagement.UserManagement;

/**
 * 
 */
public class CreateStandardUserGroupRole {

    /**
     * Creates the standard user.
     * 
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        UserManagement um = new UserManagement();
        um.addRole("admin");
        um.addGroup("admin");
        um.addUser("admin", "admin");

        um.addUserToGroup("admin", "admin", "admin");
        HibernateManager.getInstance().closeSession();

        Thread.sleep(30000);
    }
}
