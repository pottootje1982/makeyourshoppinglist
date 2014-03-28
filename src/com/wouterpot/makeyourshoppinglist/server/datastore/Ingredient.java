package com.wouterpot.makeyourshoppinglist.server.datastore;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Serialized;

import com.google.gwt.thirdparty.guava.common.base.Joiner;
import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.wouterpot.makeyourshoppinglist.helpers.RegEx;

@PersistenceCapable
public class Ingredient {
    private static Unit[] availableUnits = {
		new Unit(QuantityType.Volume, UnitType.l), new Unit(QuantityType.Volume, UnitType.dl), new Unit(QuantityType.Volume, UnitType.cl), new Unit(QuantityType.Volume, UnitType.ml),
		new Unit(QuantityType.Weight, UnitType.kg), new Unit(QuantityType.Weight, UnitType.g),
		new Unit(UnitType.tbsp),
		new Unit(UnitType.ts),
		new Unit(UnitType.splashes),
		new Unit(UnitType.drops),
		new Unit(UnitType.tl),
		new Unit(UnitType.el),
	};
	
    // Serialized as blob because I couldn't manage to create
    // cross reference between parent (since parent is embedded)
    @Serialized
	private List<Unit> units = new ArrayList<Unit>();
	
	@PrimaryKey
	@Persistent
	private String productName;

	@Persistent
	private Category parentEntity;
	
	@NotPersistent
	private double parsedAmount;
	
	@NotPersistent
	private UnitType parsedUnitType;

	public Ingredient() {
		this(null);
	}

	public Ingredient(Category category) {
		this.parentEntity = category;
	}
	
	public void parseIngredient(String ingredient) {
		String[] groups = RegEx.findGroups(ingredient, "^(\\d*[.,]?\\d*)\\s*(\\S*)\\s*(.*)$");
		
		List<String> productNameParts = new ArrayList<String>();
		parsedUnitType = null;
		parsedAmount = 0;
		if (!Strings.isNullOrEmpty(groups[0])) {
			parsedAmount = Double.parseDouble(groups[0]);
			if (!Strings.isNullOrEmpty(groups[1])) {
				parsedUnitType = UnitType.parse(groups[1]);
				if (parsedUnitType == null) {
					productNameParts.add(groups[1]);
				}
			}
			parsedUnitType = parsedUnitType != null ? parsedUnitType : UnitType.number;
		}
		else {
			if (!Strings.isNullOrEmpty(groups[1]))
				productNameParts.add(groups[1]);
			parsedUnitType = UnitType.NaN;
		}
		if (!Strings.isNullOrEmpty(groups[2]))
			productNameParts.add(groups[2]);
		productName = Joiner.on(" ").join(productNameParts);
	}

	void addUnit() {
		Unit unit = getAvailableUnit(parsedUnitType);
		Unit newUnit = new Unit(null, unit.getQuantityType(), parsedUnitType, parsedAmount);
		getUnits().add(newUnit);	
	}

	public String getProductName() {
		return productName;
	}
	
	public Unit getAvailableUnit(UnitType unitType) {
		for (Unit unit : availableUnits) {
			if (unit.getUnitType() == unitType)
				return new Unit(unit);
		}
		return new Unit(unitType);
	}

	private int getCompatibleUnit(Unit otherUnit) {
		int unitIndex = 0;
		for (Unit unit : getUnits()) {
			if (unit.isAddable(otherUnit))
				return unitIndex;
			unitIndex++;
		}
		return -1;
	}
	
	public Unit getUnit(UnitType unitType) {
		for (Unit unit : getUnits()) {
			if (unit.getUnitType() == unitType)
				return unit;
		}
		return null;
	}

	public Unit getUnit(QuantityType quantityType) {
		for (Unit unit : getUnits()) {
			if (unit.getQuantityType() == quantityType)
				return unit;
		}
		return null;
	}

	private List<Unit> getUnits() {
		return units;
	}
	
	public void add(Ingredient otherIngredient) {
		if (productName == null)
			productName = otherIngredient.productName;
		for (Unit otherUnit : otherIngredient.getUnits()) {
			int unitIndex = getCompatibleUnit(otherUnit);
			if (unitIndex != -1)
				getUnits().set(unitIndex, getUnits().get(unitIndex).add(otherUnit));
			else
				getUnits().add(otherUnit);
		}
	}

	@Override
	public String toString() {
		String unitsString = Joiner.on(" + ").join(getUnits());
		if (Strings.isNullOrEmpty(unitsString))
			return productName;
		else
			return unitsString + " " + productName.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((productName == null) ? 0 : productName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ingredient other = (Ingredient) obj;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		return true;
	}
}
