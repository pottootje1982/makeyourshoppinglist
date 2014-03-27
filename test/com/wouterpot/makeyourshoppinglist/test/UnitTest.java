package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.server.datastore.Unit;
import com.wouterpot.makeyourshoppinglist.server.datastore.UnitType;

public class UnitTest {

	@Test
	public void convertTo() {
		Unit unit1 = new Unit(UnitType.l, 2.0);
		Unit unit2 = new Unit(UnitType.dl, 3.0);
		Unit converted = unit2.convertTo(unit1);
		assertEquals(0.3, converted.getAmount(), 0.0000001);
		assertEquals(UnitType.l, converted.getUnitType());
	}
	
	@Test
	public void addDifferentUnits() {
		Unit unit1 = new Unit(UnitType.l, 2.0);
		Unit unit2 = new Unit(UnitType.ml, 1.0);
		assertEquals("2001ml", unit1.add(unit2).toString());
	}

	@Test
	public void addFourDifferentUnits() {
		Unit unit1 = new Unit(UnitType.l, 1.0);
		Unit unit2 = new Unit(UnitType.dl, 1.0);
		Unit unit3 = new Unit(UnitType.cl, 1.0);
		Unit unit4 = new Unit(UnitType.ml, 1.0);
		Unit added = unit1.add(unit2);
		assertEquals("11dl", added.toString());
		added = added.add(unit3);
		assertEquals("111cl", added.toString());
		added = added.add(unit4);
		assertEquals("1111ml", added.toString());
	}	
}
