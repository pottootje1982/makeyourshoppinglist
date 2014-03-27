package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.server.datastore.Quantity;
import com.wouterpot.makeyourshoppinglist.server.datastore.Unit;

public class QuantityTest {

	private static Quantity volume = new Quantity("volume", new Unit("l", 1000), new Unit("dl", 100), new Unit("cl", 10), new Unit("ml", 1)); 
	
	@Test
	public void testContaining() {
		assertTrue(volume.contains("l"));
		assertTrue(volume.contains("dl"));
		assertTrue(volume.contains("cl"));
		assertTrue(volume.contains("ml"));
		assertFalse(volume.contains("g"));
	}
	
	@Test
	public void testClone() throws CloneNotSupportedException {
		Quantity quantity = new Quantity(volume);
		assertTrue(quantity.contains("l"));
		assertTrue(quantity.contains("dl"));
		assertTrue(quantity.contains("cl"));
		assertTrue(quantity.contains("ml"));
	}

	@Test
	public void testAdd() throws CloneNotSupportedException {
		Quantity quantity = new Quantity(volume);
		quantity.add(new Unit("l", 1000, 3));
		assertEquals("3l", quantity.toString());
	}
}
