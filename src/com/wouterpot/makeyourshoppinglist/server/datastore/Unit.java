package com.wouterpot.makeyourshoppinglist.server.datastore;

import java.util.HashMap;
import java.util.Map;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.ibm.icu.text.DecimalFormat;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Unit {
	
    @PrimaryKey
    @Persistent
	private UnitType unitType;
    @Persistent
	private double amount;

	public UnitType getUnitType() {
		return unitType;
	}

	public int getFactor() {
		return unitType != null ? unitType.getFactor() : 1;
	}

	public double getAmount() {
		return amount;
	}

	public Unit(UnitType unitType) {
		this(unitType, 0);
	}
	
	public Unit(Unit other) {
		this(other.unitType, other.amount);
	}
	
	public Unit(UnitType unitType, double amount) {
		this.unitType = unitType;
		this.amount = amount;
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
		return String.format("%s%s", df.format(amount), unitType != null ? unitType : "");
	}
}
