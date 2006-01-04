/*
 * Copyright (c) 1997-2005 by media style GmbH
 */

package de.ingrid.usermanagement;

import java.util.HashSet;
import java.util.Set;

/**
 */
public class User {

    private String fPassword;

    private String fName;

    private Set fGroupRoleRelation = new HashSet();

    /**
     */
    public User() {
        //for hibernate
    }
    
    /**
     * @param name
     * @param password
     */
    public User(final String name, final String password) {
        this.fName = name;
        this.fPassword = password;
    }

    /**
     * @return
     */
    public String getName() {
        return this.fName;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.fName = name;
    }

    /**
     * @return
     */
    public String getPassword() {
        return this.fPassword;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.fPassword = password;
    }
}
