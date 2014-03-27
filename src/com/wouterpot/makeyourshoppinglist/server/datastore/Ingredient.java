package com.wouterpot.makeyourshoppinglist.server.datastore;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Embedded;
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
    
    private static Quantity[] availableQuantities = {
		new Quantity(QuantityType.Volume, new Unit(UnitType.l), new Unit(UnitType.dl), new Unit(UnitType.cl), new Unit(UnitType.ml)),
		new Quantity(QuantityType.Weight, new Unit(UnitType.kg), new Unit(UnitType.g)),
		new Quantity(QuantityType.MeasurableQuantity, new Unit(UnitType.tbsp)),
		new Quantity(QuantityType.MeasurableQuantity, new Unit(UnitType.ts)),
		new Quantity(QuantityType.MeasurableQuantity, new Unit(UnitType.splashes)),
		new Quantity(QuantityType.MeasurableQuantity, new Unit(UnitType.drops)),
		new Quantity(QuantityType.MeasurableQuantity, new Unit(UnitType.tl)),
		new Quantity(QuantityType.MeasurableQuantity, new Unit(UnitType.el)),
	};
    private static Quantity defaultQuantity = new Quantity(QuantityType.Countable);
	
	@Persistent(embeddedElement = "true", defaultFetchGroup = "true")
	private List<Quantity> quantities = new ArrayList<Quantity>();
	@Persistent
	private String productName;

	public Ingredient(String ingredient) {
		String[] groups = RegEx.findGroups(ingredient, "^(\\d*[.,]?\\d*)\\s*(\\S*)\\s*(.*)$");
		
		List<String> productNameParts = new ArrayList<String>();
		if (!StringUtils.isEmpty(groups[0])) {
			double amount = Double.parseDouble(groups[0]);
			UnitType unitType = null;
			if (!StringUtils.isEmpty(groups[1])) {
				unitType = UnitType.parse(groups[1]);
				if (unitType == null)
					productNameParts.add(groups[1]);
			}
			addToQuantity(unitType, amount);
		}
		if (!StringUtils.isEmpty(groups[2]))
			productNameParts.add(groups[2]);
		productName = StringUtils.join(productNameParts, " ");
		
	}

	private void addToQuantity(UnitType unitType, double amount) {
		Quantity quantity = getAvailableQuantityByUnit(unitType);
		quantity.add(new Unit(unitType, amount));
		quantities.add(quantity);	
	}

	public String getProductName() {
		return productName;
	}
	
	public Quantity getAvailableQuantityByUnit(UnitType unitType) {
		for (Quantity quantity : availableQuantities) {
			if (quantity.contains(unitType))
				return new Quantity(quantity);
		}
		return new Quantity(defaultQuantity);
	}

	private Quantity getCompatibleQuantity(Quantity otherQuantity) {
		for (Quantity quantity : quantities) {
			if (quantity.isAddable(otherQuantity))
				return quantity;
		}
		return null;
	}
	
	public Quantity getQuantity(QuantityType quantityType) {
		for (Quantity quantity : quantities) {
			if (quantity.getType() == quantityType)
				return quantity;
		}
		return null;
	}

	public Quantity getQuantity(UnitType unitType) {
		for (Quantity quantity : quantities) {
			if (quantity.getUnitType() == unitType)
				return quantity;
		}
		return null;
	}


	public void add(Ingredient otherIngredient) {
		for (Quantity quantity : quantities) {
			Quantity otherQuantity = otherIngredient.getCompatibleQuantity(quantity);
			quantity.add(otherQuantity);
		}
	}

	@Override
	public String toString() {
		return StringUtils.join(quantities, " + ") + " " + productName.toString();
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
