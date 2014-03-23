package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.config.CategoryDictionary;
import com.wouterpot.makeyourshoppinglist.helpers.RegEx;
import com.wouterpot.makeyourshoppinglist.helpers.Resource;
import com.wouterpot.makeyourshoppinglist.server.Product;

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
		Product product = categoryDictionary.getProduct(ingredient);
		return product != null ? product.getCategory() : null;
	}
	
	@Test
	public void testGetExcludeString()
	{
		String category = getCategory("[exclude]");
		assertNull(category);
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
	public void testGetCategoryWithExcludes() throws URISyntaxException, IOException {
		String category = getCategory("tomato");
		assertEquals("greengrocer", category);
	}
	
	@Test
	public void testExcludedProduct() throws URISyntaxException, IOException {
		String category = getCategory("can tomatoes");
		assertNull(category);
	}
	
	@Test
	public void testEscapeStrangeChar() {
		assertEquals("bread", RegEx.escapeStrangeChars("bréad"));
	}
	
	@Test
	public void testGetCategoryStrangeChar() {
		String category = getCategory("bréad");
		assertEquals("baker", category);
	}
		
	@Test
	public void testGetCategoryComment() {
		String category = getCategory("#herbs");
		assertNull(category);
	}
}
