package com.wouterpot.makeyourshoppinglist.test;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.wouterpot.makeyourshoppinglist.server.ShoppingListFactory;

public class DataStoreTestBase {
	private static LocalDatastoreServiceTestConfig config = new LocalDatastoreServiceTestConfig();
	private final static LocalServiceTestHelper helper =
	        new LocalServiceTestHelper(config.setDefaultHighRepJobPolicyUnappliedJobPercentage(100));
	
	protected ShoppingListFactory shoppingListFactory;

    @Before
    public void setUp() {
        helper.setUp();
		shoppingListFactory = ShoppingListFactory.get();
    }
    
    @After
    public void tearDown() {
        helper.tearDown();
    }

	@BeforeClass 
    public static void setUpClass() {      
    }
	
    @AfterClass 
    public static void tearDownClass() { 
    }
}
