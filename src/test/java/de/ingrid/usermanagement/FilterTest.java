/*
 * Copyright (c) 1997-2005 by media style GmbH
 */

package de.ingrid.usermanagement;

import junit.framework.TestCase;

/**
 *
 */
public class FilterTest extends TestCase {

    private UserManagement fUserManagement;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        this.fUserManagement = new UserManagement();
        this.fUserManagement.addUser("abdUSERfgfh", "");
        this.fUserManagement.addUser("adUSERfgh", "");
        this.fUserManagement.addUser("USERfgfh", "");
        this.fUserManagement.addUser("abdUSER", "");
        this.fUserManagement.addUser("USER", "");
        this.fUserManagement.addUser("dUSERf", "");
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
    public void testFindAll() {
        final String filter = "*USER*";

        final String[] users = this.fUserManagement.find(filter);
        assertEquals(6, users.length);
    }

    /**
     *
     */
    public void testFindStarUSER() {
        final String filter = "*USER";

        final String[] users = this.fUserManagement.find(filter);
        assertEquals(2, users.length);
    }

    /**
     *
     */
    public void testFindUSERStar() {
        final String filter = "USER*";

        final String[] users = this.fUserManagement.find(filter);
        assertEquals(2, users.length);
    }

    /**
     *
     */
    public void testFindQuestionUSERQuestion() {
        final String filter = "?USER?";

        final String[] users = this.fUserManagement.find(filter);
        assertEquals(1, users.length);
    }

    /**
     *
     */
    public void testFindaQuestiondUSERfgQuestionh() {
        final String filter = "a?dUSERfg?h";

        final String[] users = this.fUserManagement.find(filter);
        assertEquals(1, users.length);
    }

    /**
     *
     */
    public void testFindUSER() {
        final String filter = "USER";

        final String[] users = this.fUserManagement.find(filter);
        assertEquals(1, users.length);
    }
}
