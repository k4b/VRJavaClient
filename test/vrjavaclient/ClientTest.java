/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vrjavaclient;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author karol
 */
public class ClientTest {
    
    public ClientTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of testLoadParameters method, of class Client.
     */
    @Test
    public void testTestLoadParameters() {
        System.out.println("testLoadParameters");
        Client instance = new Client();
        boolean expResult = false;
        boolean result = instance.testLoadParameters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of testloadHosts method, of class Client.
     */
    @Test
    public void testTestloadHosts() {
        System.out.println("testloadHosts");
        Client instance = new Client();
        boolean expResult = false;
        boolean result = instance.testloadHosts();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}