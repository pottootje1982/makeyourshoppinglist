package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.config.CategoryDictionary;
import com.wouterpot.makeyourshoppinglist.config.ProductInfo;
import com.wouterpot.makeyourshoppinglist.helpers.Resource;
import com.wouterpot.makeyourshoppinglist.server.datastore.Ingredient;
import com.wouterpot.makeyourshoppinglist.server.datastore.Unit;
import com.wouterpot.makeyourshoppinglist.server.datastore.UnitType;

public class CategoryDictionaryTest {

	private CategoryDictionary categoryDictionary;

	@Before
	public void setup() throws URISyntaxException, IOException
	{
		categoryDictionary = new CategoryDictionary("en", "testdata");
	}
	
	@Test
	public void testGetFiles() throws IOException, URISyntaxException
	{
		String[] files = Resource.getResourceListing(getClass(), "testdata/en");
		assertEquals(2, files.length);
	}
	
	private String getCategory(String ingredient) {
		ProductInfo productInfo = categoryDictionary.getProductInfo(ingredient);
		return productInfo != null ? productInfo.getCategory() : null;
	}
	
	// Test whether [exclude] isn't returned as greengrocer (because this string is present in that file),
	// but the default category
	@Test
	public void testGetExcludeString()
	{
		String category = getCategory("[exclude]");
		assertEquals(null, category);
		category = getCategory("[EXCLUDE]");
		assertEquals(null, category);
	}

	// Test whether #herbs isn't returned as greengrocer (because this string is present in that file),
	// but the default category
	@Test
	public void testGetCategoryComment() {
		String category = getCategory("#herbs");
		assertEquals(null, category);
	}

	@Test
	public void testGetCategoryEmptyString()
	{
		String category = getCategory("");
		assertNull(category);
	}
	
	@Test
	public void testGetCategory() throws URISyntaxException, IOException {
		String category = getCategory("bread");
		assertEquals("baker", category);
	}
	
	@Test
	public void composedIngredient() {
		String category = getCategory("butternut squash");
		assertEquals("greengrocer", category);
	}


	@Test
	public void testGetCategoryWithExcludes() throws URISyntaxException, IOException {
		String category = getCategory("tomato");
		assertEquals("greengrocer", category);
	}
	
	@Test
	public void testExcludedProduct() throws URISyntaxException, IOException {
		String category = getCategory("can tomatoes");
		assertEquals(null, category);
	}
	
	@Test
	public void testPluralAndDiminitive() throws URISyntaxException, IOException {
		categoryDictionary = new CategoryDictionary("nl", "testdata");
		String category = getCategory("kipkarbonade");
		assertEquals("butcher", category);
		category = getCategory("kipkarbonades");
		assertEquals("butcher", category);
		category = getCategory("kipkarbonaadje");
		assertEquals("butcher", category);
		category = getCategory("kipkarbonaadjes");
		assertEquals("butcher", category);
	}
}
