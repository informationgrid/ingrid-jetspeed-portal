package de.ingrid.usermanagement.jetspeed;

import de.ingrid.usermanagement.jetspeed.IngridUserSecurityHandler;
import junit.framework.TestCase;

public class IngridUserSecurityHandlerTestLocal extends TestCase {

    IngridUserSecurityHandler uh;
    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        this.uh = new IngridUserSecurityHandler();
    }
    
    public void testGetUserPrincipal() {
        this.uh.getUserPrincipal("admin");
    }
    
    
    
}
