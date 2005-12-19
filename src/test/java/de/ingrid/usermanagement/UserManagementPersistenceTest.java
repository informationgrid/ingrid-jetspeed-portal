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

    private Session fSession;

    protected void setUp() {
        this.fSession = HibernateUtil.currentSession();
    }

    protected void tearDown() {
        HibernateUtil.closeSession();
    }

    /**
     *
     */
    public void testPersistenceWrite() {
        Transaction tx = this.fSession.beginTransaction();

        UserManagement um = new UserManagement();
        um.addGroup("group");
        um.addRole("role");
        um.addUser("user", "password");
        um.addUserToGroup("user", "group", "role");

        this.fSession.save(um);
        tx.commit();
    }

    /**
     *
     */
    public void testPersistenceRead() {
        Transaction tx = this.fSession.beginTransaction();

        List result = this.fSession.createQuery("from UserManagement").list();

        boolean auth = false;
        for (Iterator iter = result.iterator(); iter.hasNext();) {
            UserManagement um = (UserManagement) iter.next();
            auth = um.authenticate("user", "password");
        }
        assertTrue(auth);

        tx.commit();
    }

    /**
     *
     */
    public void testPersistenceUpdateWrite() {
        Transaction tx = this.fSession.beginTransaction();

        UserManagement um = null;
        List result = this.fSession.createQuery("from UserManagement").list();
        for (Iterator iter = result.iterator(); iter.hasNext();) {
            um = (UserManagement) iter.next();
            um.addUser("user2", "password2");
        }

        assertNotNull(um);
        this.fSession.update(um);
        tx.commit();
    }

    /**
     *
     */
    public void testPersistenceUpdateRead() {
        Transaction tx = this.fSession.beginTransaction();

        List result = this.fSession.createQuery("from UserManagement").list();

        boolean auth = false;
        for (Iterator iter = result.iterator(); iter.hasNext();) {
            UserManagement um = (UserManagement) iter.next();
            auth = (um.authenticate("user", "password") && um.authenticate("user2", "password2"));
        }
        assertTrue(auth);

        tx.commit();
    }
}
