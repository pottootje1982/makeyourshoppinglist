package com.wouterpot.makeyourshoppinglist.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.wouterpot.makeyourshoppinglist.server.ShoppingListFactory;

public class DataStoreTestBase {
	private final static LocalServiceTestHelper helper =
	        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(),
    		new LocalDatastoreServiceTestConfig().setDefaultHighRepJobPolicyUnappliedJobPercentage(100));

    @Before
    public void setUp() {
        ShoppingListFactory.get().createNewShoppingList();
    }
    
    @After
    public void tearDown() {
    }

	@BeforeClass 
    public static void setUpClass() {      
        helper.setUp();
    }
	
    @AfterClass 
    public static void tearDownClass() { 
        helper.tearDown();
    }
}
