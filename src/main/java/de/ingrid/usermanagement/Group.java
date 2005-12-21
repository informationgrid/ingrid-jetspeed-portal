/*
 * Copyright (c) 1997-2005 by media style GmbH
 */

package de.ingrid.usermanagement;

import java.util.Set;

/**
 * @hibernate.class table = groups
 */
public class Group {

    private String fName;

    private Set fRoles;

    /**
     * @param name
     */
    public Group(final String name) {
        this.fName = name;
    }

    /**
     * @return
     * @hibernate.set lazy = "false" cascade = "none"
     * @hibernate.key foreign-key = "name" column = "group_name"
     * @hibernate.many-to-many class = "de.ingrid.usermanagement.Role"
     */
    public Set getRoles() {
        return this.fRoles;
    }

    /**
     * @param roles
     */
    public void setRoles(Set roles) {
        this.fRoles = roles;
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

}
