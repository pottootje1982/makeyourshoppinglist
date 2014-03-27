package com.wouterpot.makeyourshoppinglist.server.datastore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.eclipse.jetty.webapp.FragmentDescriptor.OtherType;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Quantity {
	
    @PrimaryKey
	private QuantityType type;

    @Persistent(embeddedElement = "true", defaultFetchGroup = "true")
	private ArrayList<Unit> units = new ArrayList<Unit>();

	private Unit baseUnit;

	public Quantity(QuantityType type, Unit... units) {
		this(type, Arrays.asList(units));
	}
	
	public Quantity(QuantityType type, List<Unit> units) {
		this.type = type;
		baseUnit = units.size() > 0 ? units.get(0) : null;
		for (Unit unit : units) {
			Unit newUnit = new Unit(unit);
			this.units.add(newUnit);
			if (newUnit.getFactor() < baseUnit.getFactor())
				baseUnit = newUnit;
		}
	}

	public Quantity(Quantity other) {
		this(other.type, other.units);
	}

	public QuantityType getType() {
		return type;
	}

	public void add(Quantity quantity) {
		for (int i = 0; i < units.size(); i++) {
			units.set(i, units.get(i).add(quantity.units.get(i)));
		}
	}
	
	public boolean contains(UnitType unitType) {
		Unit unit = getUnit(unitType);
		return unit != null;
	}

	private Unit getUnit(UnitType unitType) {
		int index = getUnitIndex(unitType);
		return index != -1 ? units.get(index) : null; 
	}

	private int getUnitIndex(UnitType unitType) {
		int index = 0;
		for	(Unit unit : units) {
			if (unit.getUnitType().equals(unitType))
				return index;
			index++;
		}
		return -1;
	}

	public void add(Unit otherUnit) {
		int unitIndex = getUnitIndex(otherUnit.getUnitType());
		if (unitIndex != -1)
			units.set(unitIndex, units.get(unitIndex).add(otherUnit));
		else
			units.add(otherUnit);
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

	public UnitType getUnitType() {
		if (units.size() == 1) {
			return units.get(0).getUnitType();
		}
		return null;
	}

	public boolean isAddable(Quantity otherQuantity) {
		return type == otherQuantity.type && getUnitType() == otherQuantity.getUnitType();
	}
}
