package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.config.ProductInfo;
import com.wouterpot.makeyourshoppinglist.server.PMF;
import com.wouterpot.makeyourshoppinglist.server.datastore.Product;

public class ProductTest {
	@BeforeClass
	public static void setup()	{
		PMF.setTesting(true);
	}
	
	@AfterClass
	public static void teardown() {
		PMF.setTesting(false);
	}
	
	@Test
	public void testAddProducts() {
		Product product1 = new Product("2 el olie", new ProductInfo("olie"));
		Product product2 = new Product("4 el olie", new ProductInfo("olie"));
		product1.add(product2);
		assertEquals("6el olie", product1.toString());
	}

}
