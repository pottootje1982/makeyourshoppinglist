package com.wouterpot.makeyourshoppinglist.server.datastore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.lang3.ArrayUtils;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Quantity {
	
    @PrimaryKey
	private String name;

    @Persistent(embeddedElement = "true", defaultFetchGroup = "true")
	private Unit[] units;

	private Unit baseUnit;

	public Quantity(String name, Unit... units) {
		this.name = name;
		this.units = units;
		baseUnit = units.length > 0 ? units[0] : null;
		for (int i = 0; i < units.length; i++) {
			units[i] = new Unit(units[i]);
			if (units[i].getFactor() < baseUnit.getFactor())
				baseUnit = units[i];
		}
	}
	
	public Quantity(Quantity other) {
		this(other.name, other.units);
	}

	public String getName() {
		return name;
	}

	public void add(Quantity quantity) {
		for (int i = 0; i < units.length; i++) {
			units[i] = units[i].add(quantity.units[i]);
		}
	}
	
	public boolean contains(String unitId) {
		Unit unit = getUnit(unitId);
		return unit != null;
	}

	private Unit getUnit(String unitId) {
		int index = getUnitIndex(unitId);
		return index != -1 ? units[index] : null; 
	}

	private int getUnitIndex(String unitId) {
		int index = 0;
		for	(Unit unit : units) {
			if (unit.getId().equals(unitId))
				return index;
			index++;
		}
		return -1;
	}

	public void add(Unit otherUnit) {
		int unitIndex = getUnitIndex(otherUnit.getId());
		if (unitIndex != -1)
			units[unitIndex] = units[unitIndex].add(otherUnit);
	}
	
	@Override
	public String toString() {
		List<Unit> unitsNot0 = new ArrayList<Unit>();
		Unit added = null;
		for	(Unit unit : units) {
			if (unit.getAmount() > 0)
				if (added == null)
					added = new Unit(unit);
				else
					added.add(unit);
		}
		return added != null ? added.toString() : "";
	}
}
