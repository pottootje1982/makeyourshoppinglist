package com.wouterpot.makeyourshoppinglist.server.datastore;

import javax.jdo.annotations.Extensions;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.eclipse.jdt.internal.compiler.ast.Argument;

import com.ibm.icu.text.DecimalFormat;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Unit {
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(
        vendorName = "datanucleus",
        key        = "gae.encoded-pk",
        value      = "true"
    )
    private String      id;

    @Persistent
    @Extensions({
        @Extension(vendorName="datanucleus", key="enum-getter-by-value", value="parse"),
        @Extension(vendorName="datanucleus", key="enum-value-getter", value="getValue")
       })
	private UnitType unitType;
    
    @Persistent
	private double amount;
    
    @Persistent
    @Extensions({
        @Extension(vendorName="datanucleus", key="enum-getter-by-value", value="parse"),
        @Extension(vendorName="datanucleus", key="enum-value-getter", value="getValue")
       })
	private QuantityType quantityType;

	public Unit(UnitType unitType) {
		this(QuantityType.Countable, unitType);
	}
	
	public Unit(Unit other) {
		this(other.quantityType, other.unitType, other.amount);
	}
	
	public Unit(QuantityType quantityType, UnitType unitType) {
		this(quantityType, unitType, 0);
	}
	
	public Unit(QuantityType quantityType, UnitType unitType, double amount) {
		if (quantityType == null) throw new IllegalArgumentException("Quantity type should be defined!");
		if (unitType == null) throw new IllegalArgumentException("Unit type should be defined!");
		this.quantityType = quantityType;
		this.unitType = unitType;
		this.amount = amount;
	}

	public Unit(QuantityType quantityType) {
		this(quantityType, UnitType.number);
	}

	public Unit add(Unit other) {
		Unit result;
		double amountToAdd = other.amount;
		if (other.getFactor() < getFactor()) {
			result = this.convertTo(other);
		}
		else if (other.getFactor() > getFactor())
		{
			result = other.convertTo(this);
			amountToAdd = amount;
		}
		else
			result = new Unit(this);
		result.amount += amountToAdd;
		return result;			 
	}

	public Unit convertTo(Unit other) {
		Unit result = new Unit(other);
		result.amount = amount * ((double)getFactor()/other.getFactor());
		return result;
	}

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("#.#");
		return String.format("%s%s", df.format(amount), unitType != UnitType.number ? unitType : "");
	}
	
	public UnitType getUnitType() {
		return unitType;
	}

	public int getFactor() {
		return unitType.getFactor();
	}

	public double getAmount() {
		return amount;
	}
	
	public QuantityType getQuantityType() {
		return quantityType;
	}

	public boolean isAddable(Unit otherUnit) {
		return (quantityType != QuantityType.Countable && quantityType == otherUnit.quantityType) 
				|| unitType == otherUnit.unitType;
	}
}
