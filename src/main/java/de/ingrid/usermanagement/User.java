/*
 * Copyright (c) 1997-2005 by media style GmbH
 */

package de.ingrid.usermanagement;

import java.util.Set;

/**
 * @hibernate.class table = users
 */
public class User {

    private String fPassword;

    private String fName;

    private Set fGroups;

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
     * @hibernate.set lazy = "false" cascade = "none"
     * @hibernate.key foreign-key = "name" column = "user_name"
     * @hibernate.many-to-many class = "de.ingrid.usermanagement.Group"
     */
    public Set getGroups() {
        return this.fGroups;
    }
    
    /**
     * @param groups
     */
    public void setGroups( Set groups) {
        this.fGroups = groups;
    }

    /**
     * @return
     * @hibernate.id generator-class = "assigned" 
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
     * @hibernate.property
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
