package com.wouterpot.makeyourshoppinglist.server.datastore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Serialized;

import org.apache.commons.math.fraction.Fraction;
import org.apache.commons.math.fraction.FractionFormat;

import com.google.gwt.thirdparty.guava.common.base.Joiner;
import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.wouterpot.makeyourshoppinglist.helpers.RegEx;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Ingredient {
    private static List<Unit> availableUnits = new ArrayList<Unit>() { {
		for (UnitType unitType : UnitType.values()) {
			switch (unitType) {
			case l: add(new Unit(QuantityType.Volume, UnitType.l)); break;
			case dl: add(new Unit(QuantityType.Volume, UnitType.dl)); break;
			case cl: add(new Unit(QuantityType.Volume, UnitType.cl)); break;
			case ml: add(new Unit(QuantityType.Volume, UnitType.ml)); break;
			case kg: add(new Unit(QuantityType.Weight, UnitType.kg)); break;
			case g: add(new Unit(QuantityType.Weight, UnitType.g)); break;
			case NaN:
			case pieces: break;
			default: add(new Unit(unitType)); break;
			}
		}
	}};
	
    // Serialized as blob because I couldn't manage to create
    // cross reference between parent (since parent is embedded)
    @Serialized
	private List<Unit> units = new ArrayList<Unit>();
	
	@PrimaryKey
	@Persistent
	private String productName;

	public Ingredient(String ingredient) {
		parseIngredient(ingredient);
	}
	
	public Ingredient(Ingredient ingredient) {
		if (ingredient == null)
			throw new IllegalArgumentException("Ingredient cannot be null");
		for (Unit unit : ingredient.units) {
			units.add(unit);
		}
		productName = ingredient.productName;
	}

	private void parseIngredient(String ingredient) {
		String[] groups = RegEx.findGroups(ingredient, "^(\\d*[ .,/]*\\d*[ /¼½¾]*\\d*)\\s*(\\S*)\\s*(.*)$");
		
		List<String> productNameParts = new ArrayList<String>();
		UnitType parsedUnitType = null;
		double parsedAmount = 0;
		String numericalPart = groups[0];
		if (!Strings.isNullOrEmpty(numericalPart)) {
			try {
				parsedAmount = parseNumericalPart(numericalPart);
			}
			catch (Exception ex) {
				productNameParts.add(numericalPart);
			}
			if (!Strings.isNullOrEmpty(groups[1])) {
				parsedUnitType = UnitType.parse(groups[1]);
				if (parsedUnitType == null) {
					productNameParts.add(groups[1]);
				}
			}

			parsedUnitType = parsedUnitType != null ? parsedUnitType : UnitType.pieces;
		}
		else {
			if (!Strings.isNullOrEmpty(groups[1]))
				productNameParts.add(groups[1]);
			parsedUnitType = UnitType.NaN;
		}
		if (!Strings.isNullOrEmpty(groups[2]))
			productNameParts.add(groups[2]);
		productName = Joiner.on(" ").join(productNameParts);
		addUnit(parsedUnitType, parsedAmount);
	}

	private double parseNumericalPart(String numericalPart) throws ParseException {
		numericalPart = numericalPart.replaceAll("¼", "1/4");
		numericalPart = numericalPart.replaceAll("½", "1/2");
		numericalPart = numericalPart.replaceAll("¾", "3/4");
		if (numericalPart.contains("/")) {
			FractionFormat fractionFormat = new FractionFormat();
			try {
				Fraction fraction = fractionFormat.parse(numericalPart);
				return fraction.doubleValue();
			}
			catch (ParseException ex) {
				// Divison might have been something like 1 1/2
				String[] groups = RegEx.findGroups(numericalPart, "(\\d*) +(\\d*[ /]+\\d*)");
				double unit = fractionFormat.parse(groups[0]).doubleValue();
				double fraction = fractionFormat.parse(groups[1]).doubleValue();
				return unit + fraction;
			}
		}
		return Double.parseDouble(numericalPart);
	}

	private void addUnit(UnitType parsedUnitType, double parsedAmount) {
		if (parsedUnitType != UnitType.NaN) {
			Unit unit = getAvailableUnit(parsedUnitType);
			Unit newUnit = new Unit(unit.getQuantityType(), parsedUnitType, parsedAmount);
			getUnits().add(newUnit);	
		}
	}

	public String getProductName() {
		return productName;
	}
	
	public static Unit getAvailableUnit(UnitType unitType) {
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
