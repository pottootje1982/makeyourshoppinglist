package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.helpers.RegEx;

public class RegExTest {

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
	public void testFindGroups()
	{
		String regex = "(\\S*)(bospeen\\S*)";
		String[] groups = RegEx.findGroups("3 bospeentjes", regex);
		assertEquals(2, groups.length);
		assertEquals("", groups[0]);
		assertEquals("bospeentjes", groups[1]);
	}
}
