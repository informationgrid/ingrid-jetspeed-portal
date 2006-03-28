package de.ingrid.usermanagement.jetspeed;

import org.apache.jetspeed.security.SecurityException;

import junit.framework.TestCase;

public class IngridCredentialHandlerTest extends TestCase {

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

    }

}
