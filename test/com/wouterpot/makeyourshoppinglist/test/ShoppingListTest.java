package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.server.Product;
import com.wouterpot.makeyourshoppinglist.server.ShoppingList;
import com.wouterpot.makeyourshoppinglist.server.ShoppingListFactory;

public class ShoppingListTest {

	@Test
	public void getShoppingList() throws IOException {
		ShoppingListFactory shoppingListFactory = new ShoppingListFactory();
		File file = new File("test/testdata/pages/sites.google.com.html");
		shoppingListFactory.addToShoppingList(file);
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		List<Product> shoppingItems = shoppingList.getProducts("greengrocer");
		assertEquals(5, shoppingItems.size());
		assertEquals("3 bospeentjes", shoppingItems.get(0).getIngredient());
		assertEquals("1 meiknolletje", shoppingItems.get(1).getIngredient());
		assertEquals("100 gram peultjes", shoppingItems.get(2).getIngredient());
		assertEquals("1 kleine courgette", shoppingItems.get(3).getIngredient());
		assertEquals("2 sjalotten, gesnipperd", shoppingItems.get(4).getIngredient());
	}

	@Test
	public void initialShoppingListIsEmpty() throws IOException {
		ShoppingListFactory shoppingListFactory = new ShoppingListFactory();
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		assertTrue(shoppingList.isEmpty());
	}
	
	@Test
	public void getEnglishShoppingList() throws IOException {
		ShoppingListFactory shoppingListFactory = new ShoppingListFactory();
		File file = new File("test/testdata/pages/bbcgoodfood.com.html");
		shoppingListFactory.addToShoppingList(file);
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		List<Product> shoppingItems = shoppingList.getProducts("greengrocer");
		assertEquals(4, shoppingItems.size());
		assertEquals("3 small shallots, diced", shoppingItems.get(0).getIngredient());
		assertEquals("3 garlic cloves, 2 crushed and 1 sliced", shoppingItems.get(1).getIngredient());
		assertEquals("300g chopped and squashed tomatoes (squeeze to a pulp using your fingers)", shoppingItems.get(2).getIngredient());
		assertEquals("handful flat-leaf parsley, roughly chopped", shoppingItems.get(3).getIngredient());
	}
	
	@Test
	public void getSpecialCharRecipeList() throws IOException {
		ShoppingListFactory shoppingListFactory = new ShoppingListFactory();
		File file = new File("test/testdata/pages/sites.google.com.nl.html");
		shoppingListFactory.addToShoppingList(file);
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		List<Product> shoppingItems = shoppingList.getProducts("supermarket");
		assertEquals(4, shoppingItems.size());
		assertEquals("3 eetlepels roomboter", shoppingItems.get(0).getIngredient());
		// TODO: no good!!
		assertEquals("125 ml cr�me fra�che", shoppingItems.get(3).getIngredient());
	}


	@Test
	public void getSmulwebShoppingList() throws IOException {
		ShoppingListFactory shoppingListFactory = new ShoppingListFactory();
		File file = new File("test/testdata/pages/smulweb.nl.html");
		shoppingListFactory.addToShoppingList(file);
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		List<Product> shoppingItems = shoppingList.getProducts("greengrocer");
		
		assertEquals(5, shoppingItems.size());
		assertEquals("3 middelgrote courgettes", shoppingItems.get(0).getIngredient());
		assertEquals("vers gehakte peterselie", shoppingItems.get(4).getIngredient());
		
		shoppingItems = shoppingList.getProducts("fishmonger");
		assertEquals(1, shoppingItems.size());
		assertEquals("250 gr gepelde garnalen", shoppingItems.get(0).getIngredient());
		
		shoppingItems = shoppingList.getProducts("supermarket");
		assertEquals(4, shoppingItems.size());
		assertEquals("olijfolie", shoppingItems.get(0).getIngredient());
		assertEquals("zout en peper", shoppingItems.get(3).getIngredient());
	}
}
