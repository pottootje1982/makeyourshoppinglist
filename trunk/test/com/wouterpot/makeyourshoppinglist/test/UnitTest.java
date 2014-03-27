package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.server.datastore.Unit;

public class UnitTest {

	@Test
	public void convertTo() {
		Unit unit1 = new Unit("l", 1000, 2.0);
		Unit unit2 = new Unit("dl", 100, 3.0);
		Unit converted = unit2.convertTo(unit1);
		assertEquals(0.3, converted.getAmount(), 0.0000001);
		assertEquals("l", converted.getId());
	}
	
	@Test
	public void addDifferentUnits() {
		Unit unit1 = new Unit("l", 1000, 2.0);
		Unit unit2 = new Unit("ml", 1, 1.0);
		assertEquals("2001ml", unit1.add(unit2).toString());
	}

	@Test
	public void addFourDifferentUnits() {
		Unit unit1 = new Unit("l", 1000, 1.0);
		Unit unit2 = new Unit("dl", 100, 1.0);
		Unit unit3 = new Unit("cl", 10, 1.0);
		Unit unit4 = new Unit("ml", 1, 1.0);
		Unit added = unit1.add(unit2);
		assertEquals("11dl", added.toString());
		added = added.add(unit3);
		assertEquals("111cl", added.toString());
		added = added.add(unit4);
		assertEquals("1111ml", added.toString());
	}	
}
