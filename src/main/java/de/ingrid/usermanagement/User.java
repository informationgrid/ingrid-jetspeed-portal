/*
 * Copyright (c) 1997-2005 by media style GmbH
 */

package de.ingrid.usermanagement;

/**
 */
public class User {

    private String fPassword;

    private String fName;

    /**
     */
    public User() {
        // for hibernate
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
     * Returns the name of the user this object represents.
     * 
     * @return The name of the user.
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
     * Returns the password of the user this object represents.
     * 
     * @return The password.
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
