/*
 * Copyright (c) 1997-2005 by media style GmbH
 * 
 * $Source: $
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
public class HibernateUtil {

    private static Log fLoggger = LogFactory.getLog(HibernateUtil.class);

    private static final SessionFactory fSessionFactory;

    static {
        try {
            // Create the SessionFactory
            fSessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            fLoggger.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Comment for <code>session</code>
     */
    public static final ThreadLocal fSession = new ThreadLocal();

    /**
     * @return The current session.
     */
    public static Session currentSession() {
        Session s = (Session) fSession.get();
        // Open a new Session, if this Thread has none yet
        if (s == null) {
            s = fSessionFactory.openSession();
            fSession.set(s);
        }
        return s;
    }

    /**
     *  
     */
    public static void closeSession() {
        Session s = (Session) fSession.get();
        if (s != null) {
            s.close();
        }
        fSession.set(null);
    }
}
