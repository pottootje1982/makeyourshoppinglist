package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.Product;
import com.wouterpot.makeyourshoppinglist.ShoppingList;
import com.wouterpot.makeyourshoppinglist.ShoppingListFactory;

public class ShoppingListTest {

	@Test
	public void getShoppingList() throws IOException {
		ShoppingListFactory shoppingListFactory = new ShoppingListFactory();
		File file = new File("test/testdata/pages/sites.google.com.html");
		ShoppingList shoppingList = shoppingListFactory.createShoppingList(file);
		List<Product> shoppingItems = shoppingList.getProducts("greengrocer");
		assertEquals(5, shoppingItems.size());
		assertEquals("3 bospeentjes", shoppingItems.get(0).getIngredient());
		assertEquals("1 meiknolletje", shoppingItems.get(1).getIngredient());
		assertEquals("100 gram peultjes", shoppingItems.get(2).getIngredient());
		assertEquals("1 kleine courgette", shoppingItems.get(3).getIngredient());
		assertEquals("2 sjalotten, gesnipperd", shoppingItems.get(4).getIngredient());
	}

}
