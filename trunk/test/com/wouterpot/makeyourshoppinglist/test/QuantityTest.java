package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.server.datastore.Quantity;
import com.wouterpot.makeyourshoppinglist.server.datastore.QuantityType;
import com.wouterpot.makeyourshoppinglist.server.datastore.Unit;
import com.wouterpot.makeyourshoppinglist.server.datastore.UnitType;

public class QuantityTest {

	private static Quantity volume = new Quantity(QuantityType.Volume, 
			new Unit(UnitType.l), 
			new Unit(UnitType.dl), 
			new Unit(UnitType.cl), 
			new Unit(UnitType.ml)
	); 
	
	@Test
	public void testContaining() {
		assertTrue(volume.contains(UnitType.l));
		assertTrue(volume.contains(UnitType.dl));
		assertTrue(volume.contains(UnitType.cl));
		assertTrue(volume.contains(UnitType.ml));
		assertFalse(volume.contains(UnitType.g));
	}
	
	@Test
	public void testClone() throws CloneNotSupportedException {
		Quantity quantity = new Quantity(volume);
		assertTrue(quantity.contains(UnitType.l));
		assertTrue(quantity.contains(UnitType.dl));
		assertTrue(quantity.contains(UnitType.cl));
		assertTrue(quantity.contains(UnitType.ml));
	}

	@Test
	public void testAdd() throws CloneNotSupportedException {
		Quantity quantity = new Quantity(volume);
		quantity.add(new Unit(UnitType.l, 3));
		assertEquals("3l", quantity.toString());
	}
}
