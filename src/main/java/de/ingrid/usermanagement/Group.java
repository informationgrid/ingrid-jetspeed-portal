/*
 * Copyright (c) 1997-2005 by media style GmbH
 */

package de.ingrid.usermanagement;

/**
 */
public class Group {

    private String fName;

    /**
     */
    public Group() {
        // for hibernate
    }

    /**
     * @param name
     */
    public Group(final String name) {
        this.fName = name;
    }

    /**
     * Returns the name of the group this object represents.
     * 
     * @return The name of the group.
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
