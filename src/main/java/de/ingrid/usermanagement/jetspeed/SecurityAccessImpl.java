/* Copyright 2004 Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.ingrid.usermanagement.jetspeed;

import java.security.Principal;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jetspeed.i18n.KeyedMessage;
import org.apache.jetspeed.security.SecurityException;
import org.apache.jetspeed.security.UserPrincipal;
import org.apache.jetspeed.security.impl.UserPrincipalImpl;
import org.apache.jetspeed.security.om.InternalGroupPrincipal;
import org.apache.jetspeed.security.om.InternalRolePrincipal;
import org.apache.jetspeed.security.om.InternalUserPrincipal;
import org.apache.jetspeed.security.om.impl.InternalGroupPrincipalImpl;
import org.apache.jetspeed.security.om.impl.InternalRolePrincipalImpl;
import org.apache.jetspeed.security.om.impl.InternalUserPrincipalImpl;
import org.apache.jetspeed.security.spi.SecurityAccess;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryFactory;

/**
 * <p>
 * Provides a utility class for common SPI queries.
 * </p>
 * 
 * @author <a href="mailto:dlestrat@apache.org">David Le Strat </a>
 * @author <a href="mailto:taylor@apache.org">David Sean Taylor </a>
 */
public class SecurityAccessImpl implements SecurityAccess {

    private static final Log log = LogFactory.getLog(SecurityAccessImpl.class);

    PersistenceBroker broker = null;

    /**
     * 
     * @param repositoryPath
     */
    public SecurityAccessImpl()
    {
        broker = PersistenceBrokerFactory.defaultPersistenceBroker();
    }
    
    /**
     * Clean up Objects resources. Close the DB Connection. 
     */
    public void destroy() {
        if (broker != null) {
            broker.close();
        }
    }
    
    
    /**
     * <p>
     * Returns if a Internal UserPrincipal is defined for the user name.
     * </p>
     * 
     * @param username
     *            The user name.
     * @return true if the user is known
     */
    public boolean isKnownUser(String username) {
        UserPrincipal userPrincipal = new UserPrincipalImpl(username);
        String fullPath = userPrincipal.getFullPath();
        // Get user.
        Criteria filter = new Criteria();
        filter.addEqualTo("fullPath", fullPath);
        // The isMappingOnly must not be true.
        // We don't need the mapping only user, mapping user can't be
        // authenticated with this provider.
        // we just need the true user.
        filter.addEqualTo("isMappingOnly", Boolean.FALSE);
        Query query = QueryFactory.newQuery(InternalUserPrincipalImpl.class, filter);
        boolean isKNownUser = false;
        isKNownUser = broker.getCount(query) == 1;
        return isKNownUser;
    }

    /**
     * <p>
     * Returns the {@link InternalUserPrincipal} from the user name.
     * </p>
     * 
     * @param username
     *            The user name.
     * @return The {@link InternalUserPrincipal}.
     */
    public InternalUserPrincipal getInternalUserPrincipal(String username) {
        UserPrincipal userPrincipal = new UserPrincipalImpl(username);
        String fullPath = userPrincipal.getFullPath();
        // Get user.
        Criteria filter = new Criteria();
        filter.addEqualTo("fullPath", fullPath);
        Query query = QueryFactory.newQuery(InternalUserPrincipalImpl.class, filter);
        InternalUserPrincipal internalUser = null;
        internalUser = (InternalUserPrincipal) broker.getObjectByQuery(query);
        return internalUser;
    }

    /**
     * <p>
     * Returns the {@link InternalUserPrincipal} from the user name.
     * </p>
     * 
     * @param username
     *            The user name.
     * @param isMappingOnly
     *            Whether a principal's purpose is for security mappping only.
     * @return The {@link InternalUserPrincipal}.
     */
    public InternalUserPrincipal getInternalUserPrincipal(String username, boolean isMappingOnly) {
        UserPrincipal userPrincipal = new UserPrincipalImpl(username);
        String fullPath = userPrincipal.getFullPath();
        // Get user.
        Criteria filter = new Criteria();
        filter.addEqualTo("fullPath", fullPath);
        filter.addEqualTo("isMappingOnly", new Boolean(isMappingOnly));
        Query query = QueryFactory.newQuery(InternalUserPrincipalImpl.class, filter);
        InternalUserPrincipal internalUser = null;
        internalUser = (InternalUserPrincipal) broker.getObjectByQuery(query);
        return internalUser;
    }

    /**
     * <p>
     * Returns a collection of {@link Principal}given the filter.
     * </p>
     * 
     * @param filter
     *            The filter.
     * @return Collection of {@link InternalUserPrincipal}.
     */
    public Iterator getInternalUserPrincipals(String filter) {
        Criteria queryCriteria = new Criteria();
        queryCriteria.addEqualTo("isMappingOnly", new Boolean(false));
        queryCriteria.addLike("fullPath", UserPrincipal.PREFS_USER_ROOT + filter + "%");
        Query query = QueryFactory.newQuery(InternalUserPrincipalImpl.class, queryCriteria);
        Iterator result = null;
        result = broker.getIteratorByQuery(query);
        return result;
    }

    /**
     * <p>
     * Sets the given {@link InternalUserPrincipal}.
     * </p>
     * 
     * @param internalUser
     *            The {@link InternalUserPrincipal}.
     * @param isMappingOnly
     *            Whether a principal's purpose is for security mappping only.
     * @throws SecurityException
     *             Throws a {@link SecurityException}.
     */
    public void setInternalUserPrincipal(InternalUserPrincipal internalUser, boolean isMappingOnly)
            throws SecurityException {
        try {
            if (isMappingOnly) {
                internalUser.setMappingOnly(isMappingOnly);
            }
            broker.beginTransaction();
            broker.store(internalUser);
            broker.commitTransaction();
        } catch (Exception e) {
            KeyedMessage msg = SecurityException.UNEXPECTED.create("SecurityAccess.setInternalUserPrincipal", "store",
                    e.getMessage());
            log.error(msg, e);
            if (broker != null) {
                broker.abortTransaction();
            }
            throw new SecurityException(msg, e);
        }
    }

    /**
     * <p>
     * Remove the given {@link InternalUserPrincipal}.
     * </p>
     * 
     * @param internalUser
     *            The {@link InternalUserPrincipal}.
     * @throws SecurityException
     *             Throws a {@link SecurityException}.
     */
    public void removeInternalUserPrincipal(InternalUserPrincipal internalUser) throws SecurityException {
        try {
            // Remove user.
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.beginTransaction();
            broker.delete(internalUser);
            broker.commitTransaction();
            if (log.isDebugEnabled()) {
                log.debug("Deleted user: " + internalUser.getFullPath());
            }

        } catch (Exception e) {
            KeyedMessage msg = SecurityException.UNEXPECTED.create("SecurityAccess.removeInternalUserPrincipal",
                    "store", e.getMessage());
            log.error(msg, e);
            if (broker != null) {
                broker.abortTransaction();
            }
            throw new SecurityException(msg, e);
        }
    }

    /**
     * <p>
     * Returns the {@link InternalRolePrincipal}from the role full path name.
     * </p>
     * 
     * @param roleFullPathName
     *            The role full path name.
     * @return The {@link InternalRolePrincipal}.
     */
    public InternalRolePrincipal getInternalRolePrincipal(String roleFullPathName) {
        Criteria filter = new Criteria();
        filter.addEqualTo("fullPath", roleFullPathName);
        Query query = QueryFactory.newQuery(InternalRolePrincipalImpl.class, filter);
        InternalRolePrincipal internalRole = null;
        internalRole = (InternalRolePrincipal) broker.getObjectByQuery(query);
        return internalRole;
    }

    /**
     * <p>
     * Sets the given {@link InternalRolePrincipal}.
     * </p>
     * 
     * @param internalRole
     *            The {@link InternalRolePrincipal}.
     * @param isMappingOnly
     *            Whether a principal's purpose is for security mappping only.
     * @throws SecurityException
     *             Throws a {@link SecurityException}.
     */
    public void setInternalRolePrincipal(InternalRolePrincipal internalRole, boolean isMappingOnly)
            throws SecurityException {
        try {
            if (isMappingOnly) {
                internalRole.setMappingOnly(isMappingOnly);
            }
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.beginTransaction();
            broker.store(internalRole);
            broker.commitTransaction();
        } catch (Exception e) {
            KeyedMessage msg = SecurityException.UNEXPECTED.create("SecurityAccess.setInternalRolePrincipal", "store",
                    e.getMessage());
            log.error(msg, e);
            if (broker != null) {
                broker.abortTransaction();
            }
            throw new SecurityException(msg, e);
        }
    }

    /**
     * <p>
     * Remove the given {@link InternalRolePrincipal}.
     * </p>
     * 
     * @param internalRole
     *            The {@link InternalRolePrincipal}.
     * @throws SecurityException
     *             Throws a {@link SecurityException}.
     */
    public void removeInternalRolePrincipal(InternalRolePrincipal internalRole) throws SecurityException {
        try {
            // Remove role.
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.beginTransaction();
            broker.delete(internalRole);
            broker.commitTransaction();
            if (log.isDebugEnabled()) {
                log.debug("Deleted role: " + internalRole.getFullPath());
            }

        } catch (Exception e) {
            KeyedMessage msg = SecurityException.UNEXPECTED.create("SecurityAccess.removeInternalRolePrincipal",
                    "store", e.getMessage());
            log.error(msg, e);
            if (broker != null) {
                broker.abortTransaction();
            }
            throw new SecurityException(msg, e);
        }

    }

    /**
     * <p>
     * Returns the {@link InternalGroupPrincipal}from the group full path name.
     * </p>
     * 
     * @param groupFullPathName
     *            The group full path name.
     * @return The {@link InternalGroupPrincipal}.
     */
    public InternalGroupPrincipal getInternalGroupPrincipal(String groupFullPathName) {
        Criteria filter = new Criteria();
        filter.addEqualTo("fullPath", groupFullPathName);
        Query query = QueryFactory.newQuery(InternalGroupPrincipalImpl.class, filter);
        InternalGroupPrincipal internalGroup = null;
        internalGroup = (InternalGroupPrincipal) broker.getObjectByQuery(query);
        return internalGroup;
    }

    /**
     * <p>
     * Sets the given {@link InternalGroupPrincipal}.
     * </p>
     * 
     * @param internalGroup
     *            The {@link InternalGroupPrincipal}.
     * @param isMappingOnly
     *            Whether a principal's purpose is for security mappping only.
     * @throws SecurityException
     *             Throws a {@link SecurityException}.
     */
    public void setInternalGroupPrincipal(InternalGroupPrincipal internalGroup, boolean isMappingOnly)
            throws SecurityException {
        try {

            if (isMappingOnly) {
                internalGroup.setMappingOnly(isMappingOnly);
            }
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.beginTransaction();
            broker.store(internalGroup);
            broker.commitTransaction();
        } catch (Exception e) {
            KeyedMessage msg = SecurityException.UNEXPECTED.create("SecurityAccess.setInternalGroupPrincipal", "store",
                    e.getMessage());
            log.error(msg, e);
            if (broker != null) {
                broker.abortTransaction();
            }
            throw new SecurityException(msg, e);
        }
    }

    /**
     * <p>
     * Remove the given {@link InternalGroupPrincipal}.
     * </p>
     * 
     * @param internalGroup
     *            The {@link InternalGroupPrincipal}.
     * @throws SecurityException
     *             Throws a {@link SecurityException}.
     */
    public void removeInternalGroupPrincipal(InternalGroupPrincipal internalGroup) throws SecurityException {
        try {
            // Remove role.
            broker = PersistenceBrokerFactory.defaultPersistenceBroker();
            broker.beginTransaction();
            broker.delete(internalGroup);
            broker.commitTransaction();

            if (log.isDebugEnabled()) {
                log.debug("Deleted group: " + internalGroup.getFullPath());
            }

        } catch (Exception e) {
            KeyedMessage msg = SecurityException.UNEXPECTED.create("SecurityAccess.removeInternalGroupPrincipal",
                    "store", e.getMessage());
            log.error(msg, e);
            if (broker != null) {
                broker.abortTransaction();
            }
            throw new SecurityException(msg, e);
        }

    }

    public Iterator getInternalRolePrincipals(String filter) {
        Criteria queryCriteria = new Criteria();
        queryCriteria.addEqualTo("isMappingOnly", new Boolean(false));
        queryCriteria.addLike("fullPath", UserPrincipal.PREFS_ROLE_ROOT + filter + "%");
        Query query = QueryFactory.newQuery(InternalRolePrincipalImpl.class, queryCriteria);
        Collection c = broker.getCollectionByQuery(query);
        return c.iterator();
    }

    public Iterator getInternalGroupPrincipals(String filter) {

        Criteria queryCriteria = new Criteria();
        queryCriteria.addEqualTo("isMappingOnly", new Boolean(false));
        queryCriteria.addLike("fullPath", UserPrincipal.PREFS_GROUP_ROOT + filter + "%");
        Query query = QueryFactory.newQuery(InternalGroupPrincipalImpl.class, queryCriteria);
        Collection c = broker.getCollectionByQuery(query);
        return c.iterator();
    }

}