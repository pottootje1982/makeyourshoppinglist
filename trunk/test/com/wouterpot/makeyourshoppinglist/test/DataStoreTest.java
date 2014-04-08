package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.server.PMF;
import com.wouterpot.makeyourshoppinglist.server.ShoppingListFactory;
import com.wouterpot.makeyourshoppinglist.server.datastore.Product;
import com.wouterpot.makeyourshoppinglist.server.datastore.ShoppingList;

public class DataStoreTest extends DataStoreTestBase {
	
	@Before
	public void setup() {
		super.setUp();
		ShoppingListFactory.get().createNewShoppingList();
	}
		
    @Test
    public void storeShoppingList() {
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		File file = new File("test/testdata/pages/sites.google.com.nl.html");
		shoppingListFactory.addToShoppingList(file);
		file = new File("test/testdata/pages/smulweb.nl.html");
		shoppingListFactory.addToShoppingList(file);
    }
    
	@Test
	public void testNrcNlTooLong() {
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		File file = new File("test/testdata/pages/nrc.nl.toolong.html");
		shoppingListFactory.addToShoppingList(file);
	}
    
	@Test
	public void loadPersistedShoppingList() {
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		File file = new File("test/testdata/pages/smulweb.nl.html");
		shoppingListFactory.addToShoppingList(file);
		
		PMF.open();
		List<ShoppingList> shoppingLists = PMF.retrieveAll(ShoppingList.class);
		assertEquals(1, shoppingLists.size());
		ShoppingList shoppingList = shoppingLists.get(shoppingLists.size()-1);
		PMF.retrieve(shoppingList);
		shoppingList = PMF.getObjectById(ShoppingList.class, shoppingList.getId());
		List<Product> products = shoppingList.getProducts("greengrocer");
		assertEquals(5, products.size());
		PMF.close();
	}
}
