package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.server.datastore.Category;

public class CategoryTest {

	@Test
	public void testCategoryEquals() {
		Category category = new Category("supermarket");
		assertTrue(category.equals("supermarket"));
		assertFalse(category.equals("greengrocer"));
	}

}
