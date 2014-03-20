package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.Product;
import com.wouterpot.makeyourshoppinglist.ShoppingList;
import com.wouterpot.makeyourshoppinglist.ShoppingListFactory;

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
	public void test() throws IOException {
		Document document = Jsoup.parse(new File("test/testdata/pages/bbcgoodfood.com.html"), "UTF-8");
		Elements elementsByClass = document.getElementsByAttributeValue("class", "recipe-ingredients separator separator-dashed-bottom");
		elementsByClass.size();
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


}
