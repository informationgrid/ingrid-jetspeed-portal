/*
 * Copyright (c) 1997-2005 by media style GmbH
 */

package de.ingrid.usermanagement;

/**
 * @hibernate.class table = roles
 */
public class Role {

    private String fName;

    /**
     */
    public Role() {
        //for hibernate
    }
    
    /**
     * @param name
     */
    public Role( final String name) {
        this.fName = name;
    }

    /**
     * @return The role name.
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
}
