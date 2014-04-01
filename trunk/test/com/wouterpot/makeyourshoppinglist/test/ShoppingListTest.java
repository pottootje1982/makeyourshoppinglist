package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.server.ShoppingListFactory;
import com.wouterpot.makeyourshoppinglist.server.datastore.Product;
import com.wouterpot.makeyourshoppinglist.server.datastore.ShoppingList;
import com.wouterpot.makeyourshoppinglist.shared.ProductDto;
import com.wouterpot.makeyourshoppinglist.shared.ShoppingListDto;

public class ShoppingListTest extends DataStoreTestBase {
	
	@Before
	public void setup()	{
		ShoppingListFactory.get().createNewShoppingList();
	}
	
	@Test
	public void getShoppingList() throws IOException {
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		File file = new File("test/testdata/pages/sites.google.com.html");
		shoppingListFactory.addToShoppingList(file);
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		List<Product> shoppingItems = shoppingList.getProducts("greengrocer");
		assertEquals(5, shoppingItems.size());
		assertEquals("3 bospeentjes", shoppingItems.get(0).toString());
		assertEquals("1 meiknolletje", shoppingItems.get(1).toString());
		assertEquals("100g peultjes", shoppingItems.get(2).toString());
		assertEquals("1 kleine courgette", shoppingItems.get(3).toString());
		assertEquals("2 sjalotten, gesnipperd", shoppingItems.get(4).toString());
	}

	@Test
	public void initialShoppingListIsEmpty() throws IOException {
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		assertTrue(shoppingList.isEmpty());
	}
	
	@Test
	public void getEnglishShoppingList() throws IOException {
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		File file = new File("test/testdata/pages/bbcgoodfood.com.html");
		shoppingListFactory.addToShoppingList(file);
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		List<Product> shoppingItems = shoppingList.getProducts("greengrocer");
		assertEquals(4, shoppingItems.size());
		assertEquals("3 small shallots, diced", shoppingItems.get(0).toString());
		assertEquals("3 garlic cloves, 2 crushed and 1 sliced", shoppingItems.get(1).toString());
		assertEquals("300g chopped and squashed tomatoes (squeeze to a pulp using your fingers)", shoppingItems.get(2).toString());
		assertEquals("handful flat-leaf parsley, roughly chopped", shoppingItems.get(3).toString());
	}
	
	@Test
	public void getSpecialCharRecipeList() throws IOException {
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		File file = new File("test/testdata/pages/sites.google.com.nl.html");
		shoppingListFactory.addToShoppingList(file);
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		List<Product> shoppingItems = shoppingList.getProducts("supermarket");
		assertEquals(4, shoppingItems.size());
		assertEquals("3 eetlepels roomboter", shoppingItems.get(0).toString());
		// TODO: no good!!
		assertEquals("125ml cr�me fra�che", shoppingItems.get(3).toString());

	}


	@Test
	public void getSmulwebShoppingList() throws IOException {
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		File file = new File("test/testdata/pages/smulweb.nl.html");
		shoppingListFactory.addToShoppingList(file);
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		List<Product> shoppingItems = shoppingList.getProducts("greengrocer");
		
		assertEquals(5, shoppingItems.size());
		assertEquals("3 middelgrote courgettes", shoppingItems.get(0).toString());
		assertEquals("vers gehakte peterselie", shoppingItems.get(4).toString());
		
		shoppingItems = shoppingList.getProducts("fishmonger");
		assertEquals(1, shoppingItems.size());
		assertEquals("250 gr gepelde garnalen", shoppingItems.get(0).toString());
		
		shoppingItems = shoppingList.getProducts("supermarket");
		assertEquals(4, shoppingItems.size());
		assertEquals("olijfolie", shoppingItems.get(0).toString());
		assertEquals("zout en peper", shoppingItems.get(3).toString());
	}
	
	@Test
	public void getAggregatedShoppingList() throws IOException {
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		File file = new File("test/testdata/pages/sites.google.com.html");
		shoppingListFactory.addToShoppingList(file);
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		ShoppingListDto shoppingListDto = shoppingList.getShoppingList();
		Map<String, ArrayList<ProductDto>> shoppingItems = shoppingListDto.getShoppingListMap();
		
		ArrayList<ProductDto> products = shoppingItems.get("greengrocer");
		assertEquals(5, products.size());
		assertEquals("3 bospeentjes", products.get(0).toString());
		assertEquals("1 kleine courgette", products.get(4).toString());
		
		products = shoppingItems.get("fishmonger");
		assertEquals(1, products.size());
		assertEquals("4 dauradefilets (of een andere vis)", products.get(0).toString());
		
		products = shoppingItems.get("supermarket");
		assertEquals(4, products.size());
		assertEquals("125ml slagroom", products.get(0).toString());
		assertEquals("3 eetlepels roomboter", products.get(3).toString());
	}

	
	@Test
	public void aggregate() {
		ShoppingList shoppingList = ShoppingListFactory.get().getShoppingList();
		List<String> ingredients = Arrays.asList("2 el olie", "4 el olie");
		shoppingList.addIngredients("recipe1", ingredients, "nl");
		List<Product> products = shoppingList.getProducts("supermarket");
		assertEquals(2, products.size());
		Product product1 = products.get(0);
		Product product2 = products.get(1);
		
		ShoppingListDto shoppingListDto = shoppingList.getShoppingList();
		Map<String, ArrayList<ProductDto>> shoppingListMap = shoppingListDto.getShoppingListMap();
		ArrayList<ProductDto> productDtos = shoppingListMap.get("supermarket");
		assertEquals(1, productDtos.size());
		ProductDto productDto = productDtos.get(0);
		assertArrayEquals(new String[]{product1.getId(), product2.getId()}, productDto.getIds().toArray());		
	}
}
