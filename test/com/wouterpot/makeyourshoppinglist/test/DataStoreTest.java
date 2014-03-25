package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.wouterpot.makeyourshoppinglist.server.PMF;
import com.wouterpot.makeyourshoppinglist.server.ShoppingListFactory;
import com.wouterpot.makeyourshoppinglist.server.datastore.Product;
import com.wouterpot.makeyourshoppinglist.server.datastore.ShoppingList;

public class DataStoreTest {

	private final LocalServiceTestHelper helper =
	        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
    @Before
    public void setUp() {
        helper.setUp();
        
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query newQuery = pm.newQuery(ShoppingList.class);
		List<ShoppingList> shoppingLists = (List<ShoppingList>)newQuery.execute();
		pm.deletePersistentAll(shoppingLists);
		pm.close();
    }
    
    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void storeShoppingList() {
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		File file = new File("test/testdata/pages/sites.google.com.nl.html");
		shoppingListFactory.addToShoppingList(file);
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
    }
    
	@Test
	public void loadPersistedShoppingList() {
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		File file = new File("test/testdata/pages/sites.google.com.nl.html");
		shoppingListFactory.addToShoppingList(file);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query newQuery = pm.newQuery(ShoppingList.class);
		List<ShoppingList> shoppingLists = (List<ShoppingList>)newQuery.execute();
		pm.retrieveAll(shoppingLists);
		ShoppingList shoppingList = null;
		if (shoppingLists.size() > 0) {
			shoppingList = shoppingLists.get(0);
			pm.retrieve(shoppingList);
		}
		ArrayList<Product> products = shoppingList.getProducts();
		assertEquals(10, products.size());
		pm.close();
	}
}
