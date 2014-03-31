package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.server.PMF;
import com.wouterpot.makeyourshoppinglist.server.ShoppingListFactory;
import com.wouterpot.makeyourshoppinglist.server.datastore.Product;
import com.wouterpot.makeyourshoppinglist.server.datastore.ShoppingList;

public class DataStoreTest extends DataStoreTestBase {
	
    @Test
    public void storeShoppingList() {
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		File file = new File("test/testdata/pages/sites.google.com.nl.html");
		shoppingListFactory.addToShoppingList(file);
		file = new File("test/testdata/pages/smulweb.nl.html");
		shoppingListFactory.addToShoppingList(file);
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
    }
    
	@Test
	public void loadPersistedShoppingList() {
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		File file = new File("test/testdata/pages/smulweb.nl.html");
		shoppingListFactory.addToShoppingList(file);
		
		PMF.open();
		PMF.begin();
		List<ShoppingList> shoppingLists = PMF.retrieveAll(ShoppingList.class);
		assertEquals(1, shoppingLists.size());
		ShoppingList shoppingList = shoppingLists.get(shoppingLists.size()-1);
		PMF.retrieve(shoppingList);
		List<Product> products = shoppingList.getProducts("greengrocer");
		assertEquals(5, products.size());
		PMF.commit();
		PMF.close();
	}
}
