/*
 * **************************************************-
 * InGrid Portal Security Provider
 * ==================================================
 * Copyright (C) 2014 - 2015 wemove digital solutions GmbH
 * ==================================================
 * Licensed under the EUPL, Version 1.1 or – as soon they will be
 * approved by the European Commission - subsequent versions of the
 * EUPL (the "Licence");
 * 
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * 
 * http://ec.europa.eu/idabc/eupl5
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 * **************************************************#
 */
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
