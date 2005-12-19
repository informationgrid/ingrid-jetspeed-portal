/*
 * Copyright (c) 1997-2005 by media style GmbH
 */

package de.ingrid.usermanagement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 */
public final class HibernateUtil {

    private static Log fLoggger = LogFactory.getLog(HibernateUtil.class);

    private static final SessionFactory SESSIONFACTORY;

    private HibernateUtil() {
        super();
    }

    static {
        try {
            // Create the SessionFactory
            SESSIONFACTORY = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            fLoggger.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Comment for <code>session</code>
     */
    public static final ThreadLocal SESSION = new ThreadLocal();

    /**
     * @return The current session.
     */
    public static Session currentSession() {
        Session s = (Session) SESSION.get();
        // Open a new Session, if this Thread has none yet
        if (s == null) {
            s = SESSIONFACTORY.openSession();
            SESSION.set(s);
        }
        return s;
    }

    /**
     *
     */
    public static void closeSession() {
        Session s = (Session) SESSION.get();
        if (s != null) {
            s.close();
        }
        SESSION.set(null);
    }
}
