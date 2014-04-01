package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.config.ProductInfo;
import com.wouterpot.makeyourshoppinglist.server.datastore.Ingredient;
import com.wouterpot.makeyourshoppinglist.server.datastore.Product;

public class ProductTest {
	
	@Test
	public void testAddProducts() {
		Product product1 = new Product(new ProductInfo("olie"));
		product1.setIngredient(new Ingredient("2 el olie"));
		Product product2 = new Product(new ProductInfo("olie"));
		product2.setIngredient(new Ingredient("4 el olie"));
		product1.add(product2);
		assertEquals("6el olie", product1.add(product2).toString());
	}
	
	@Test
	public void testAddUnquantifiableToVolume() {
		Product product1 = new Product(new ProductInfo("oil"));
		product1.setIngredient(new Ingredient("olive oil"));
		Product product2 = new Product(new ProductInfo("oil"));
		product2.setIngredient(new Ingredient("50ml olive oil, for frying"));
		assertEquals("50ml olive oil", product1.add(product2).toString());
	}
	
	
	// TODO: add sap van 1/2 citroen
	// & fijngeraspte schil en sap van 1 grote citroen

}
