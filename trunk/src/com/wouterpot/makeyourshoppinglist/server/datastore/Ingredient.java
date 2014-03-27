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
		new Quantity("volume", new Unit("l", 1000), new Unit("dl", 100), new Unit("cl", 10), new Unit("ml", 1)),
		new Quantity("weight", new Unit("kg", 1000), new Unit("g", 1)),
		new Quantity("tbsp"),
		new Quantity("tsp"),
		new Quantity("splashes"),
		new Quantity("drops"),
		new Quantity("tl"),
		new Quantity("el"),
	};
	
	@Persistent(embeddedElement = "true", defaultFetchGroup = "true")
	private List<Quantity> quantities = new ArrayList<Quantity>();
	@Persistent
	private String productName;

	public Ingredient(String ingredient) {
		String[] groups = RegEx.findGroups(ingredient, "^(\\d+[.,]?\\d*)\\s*(\\S*)\\s*(.*)$");
		double amount;
		String measure;
		if (!StringUtils.isEmpty(groups[0])) {
			amount = Double.parseDouble(groups[0]);
			if (!StringUtils.isEmpty(groups[1])) {
				measure = groups[1];
				Quantity quantity = getAvailableQuantityByUnit(measure);
				if (quantity == null)
					quantity = getQuantity(measure);
				quantity.add(new Unit(measure, amount));
				if (!StringUtils.isEmpty(groups[2]))
					productName = groups[2];
			}
		}
		else
			productName = groups[1] + " " + groups[2];
		
	}

	public String getProductName() {
		return productName;
	}
	
	public Quantity getAvailableQuantityByUnit(String unitId) {
		for (Quantity quantity : availableQuantities) {
			if (quantity.contains(unitId))
				return quantity;
		}
		return null;
	}

	public Quantity getAvailableQuantityByName(String name) {
		for (Quantity quantity : availableQuantities) {
			if (quantity.getName().equals(name))
				return quantity;
		}
		return null;
	}

	public Quantity getQuantity(String name) {
		for (Quantity quantity : quantities) {
			if (quantity.getName().equals(name))
				return quantity;
		}
		return null;
	}

	public void add(Ingredient otherIngredient) {
		for (Quantity quantity : quantities) {
			Quantity otherQuantity = otherIngredient.getQuantity(quantity.getName());
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
