package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.config.ProductInfo;

public class ProductInfoTest {

	private boolean matches(String productKey, String ingredient) {
		ProductInfo productInfo = new ProductInfo(productKey, "greengrocer", false, false);
		return productInfo.productMatches(ingredient);
	}
	
	@Test
	public void testProductMatches() {
		assertTrue(matches("bospeen", "3 bospeentjes"));
	}
	
	@Test
	public void testGetCategorySubString() {
		assertFalse(matches("sla", "slagroom"));
	}
	
	@Test
	public void testGetCategoryMiddleSubString() {
		assertFalse(matches("sage", "sausage"));
	}
}
