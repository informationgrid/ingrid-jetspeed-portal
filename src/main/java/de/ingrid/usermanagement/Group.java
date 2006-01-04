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
        //for hibernate
    }
    
    /**
     * @param name
     */
    public Group(final String name) {
        this.fName = name;
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
}
