package de.ingrid.usermanagement.jetspeed;

import org.apache.jetspeed.security.SecurityException;

import junit.framework.TestCase;

public class IngridCredentialHandlerTestLocal extends TestCase {

    IngridCredentialHandler ch;
    
    protected void setUp() throws Exception {
        super.setUp();
        ch = new IngridCredentialHandler();
    }

    /*
     * Test method for 'de.ingrid.usermanagement.jetspeed.IngridCredentialHandler.authenticate(String, String)'
     */
    public void testAuthenticate() throws SecurityException {
        
        assertEquals(ch.authenticate("admin", "admin"), true);
        ch.setPassword("admin", "admin", "admin1");
        assertEquals(ch.authenticate("admin", "admin1"), true);
        ch.setPassword("admin", "admin1", "admin");
        assertEquals(ch.authenticate("admin", "admin"), true);
    }

}
