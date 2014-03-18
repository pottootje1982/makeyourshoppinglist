package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.CategoryDictionary;
import com.wouterpot.makeyourshoppinglist.RegEx;

public class CategoryDictionaryTest {

	private CategoryDictionary categoryDictionary;

	@Before
	public void setup() throws URISyntaxException, IOException
	{
		categoryDictionary = new CategoryDictionary("en", "testdata");
	}
	
	@Test
	public void testMatch()
	{
		String regex = "(\\[.*\\])";
		String match = RegEx.match("[SECTION]", regex);
		assertEquals("[SECTION]", match);
	}
	
	@Test
	public void testFindContaining()
	{
		String match = RegEx.find("tomatoes", "tomato");
		assertEquals("tomato", match);
	}
	
	@Test
	public void testFindPlural()
	{
		String match = RegEx.find("tomatoes", "tomatoe?s");
		assertEquals("tomatoes", match);
		match = RegEx.find("tomatos", "tomatoe?s");
		assertEquals("tomatos", match);
	}
	
	@Test
	public void testGetFiles() throws IOException, URISyntaxException
	{
		String[] files = Resources.getResourceListing(getClass(), "testdata/en");
		assertEquals(2, files.length);
	}
	
	@Test
	public void testGetExcludeString()
	{
		String category = categoryDictionary.getCategory("[exclude]");
		assertNull(category);
	}
	
	@Test
	public void testGetCategoryEmptyString()
	{
		String category = categoryDictionary.getCategory("");
		assertNull(category);
	}
	
	@Test
	public void testGetCategory() throws URISyntaxException, IOException {
		String category = categoryDictionary.getCategory("bread");
		assertEquals("baker", category);
	}
	
	@Test
	public void testGetCategoryWithExcludes() throws URISyntaxException, IOException {
		String category = categoryDictionary.getCategory("tomato");
		assertEquals("greengrocer", category);
	}
	
	@Test
	public void testExcludedProduct() throws URISyntaxException, IOException {
		String category = categoryDictionary.getCategory("can tomatoes");
		assertNull(category);
	}
	
	@Test
	public void testEscapeStrangeChar() {
		assertEquals("bread", RegEx.escapeStrangeChars("bréad"));
	}
	
	@Test
	public void testGetCategoryStrangeChar() {
		String category = categoryDictionary.getCategory("bréad");
		assertEquals("baker", category);
	}
	
	@Test
	public void testGetCategorySubString() {
		String category = categoryDictionary.getCategory("slagroom");
		assertNull(category);
	}
	
	@Test
	public void testGetCategoryMiddleSubString() {
		String category = categoryDictionary.getCategory("sausage");
		assertNull(category);
	}
	
	@Test
	public void testGetCategorySage() {
		String category = categoryDictionary.getCategory("sage");
		assertEquals("greengrocer", category);
	}
	
	@Test
	public void testGetCategoryComment() {
		String category = categoryDictionary.getCategory("#herbs");
		assertNull(category);
	}
}
