package com.wouterpot.makeyourshoppinglist.server.datastore;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.lang3.StringUtils;

import com.wouterpot.makeyourshoppinglist.helpers.RegEx;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Ingredient {
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(
        vendorName = "datanucleus",
        key        = "gae.encoded-pk",
        value      = "true"
    )
    private String      id;
    
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
	
	@NotPersistent//(mappedBy = "parent")
    @Element(dependent = "true")
	private List<Unit> units = new ArrayList<Unit>();
	@Persistent
	private String productName;

	public Ingredient(String ingredient) {
		String[] groups = RegEx.findGroups(ingredient, "^(\\d*[.,]?\\d*)\\s*(\\S*)\\s*(.*)$");
		
		List<String> productNameParts = new ArrayList<String>();
		UnitType unitType = null;
		double amount = 0;
		if (!StringUtils.isEmpty(groups[0])) {
			amount = Double.parseDouble(groups[0]);
			if (!StringUtils.isEmpty(groups[1])) {
				unitType = UnitType.parse(groups[1]);
				if (unitType == null) {
					productNameParts.add(groups[1]);
				}
			}
			unitType = unitType != null ? unitType : UnitType.number;
		}
		else {
			if (!StringUtils.isEmpty(groups[1]))
				productNameParts.add(groups[1]);
			unitType = UnitType.NaN;
		}
		if (!StringUtils.isEmpty(groups[2]))
			productNameParts.add(groups[2]);
		addUnit(unitType, amount);
		productName = StringUtils.join(productNameParts, " ");
		
	}

	private void addUnit(UnitType unitType, double amount) {
		Unit unit = getAvailableUnit(unitType);
		Unit newUnit = new Unit(unit.getQuantityType(), unitType, amount);
		newUnit.setParent(this);
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
		String unitsString = StringUtils.join(getUnits(), " + ");
		if (StringUtils.isEmpty(unitsString))
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
