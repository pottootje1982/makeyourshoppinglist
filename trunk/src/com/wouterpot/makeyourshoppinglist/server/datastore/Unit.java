package com.wouterpot.makeyourshoppinglist.server.datastore;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.ibm.icu.text.DecimalFormat;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Unit implements Cloneable {

    @PrimaryKey
    @Persistent
	private String id;
    @Persistent
	private int factor;
    @Persistent
	private double amount;

	public String getId() {
		return id;
	}

	public int getFactor() {
		return factor;
	}

	public double getAmount() {
		return amount;
	}

	public Unit(String id) {
		this(id, 0, 0);
	}

	public Unit(String id, int factor) {
		this(id, factor, 0);
	}
	
	public Unit(Unit other) {
		this(other.id, other.factor, other.amount);
	}
	
	public Unit(String id, int factor, double amount) {
		this.id = id;
		this.factor = factor;
		this.amount = amount;
	}

	public Unit add(Unit other) {
		Unit result;
		if (other.factor < factor) {
			result = this.convertTo(other);
			result.amount += other.amount;
		}
		else
		{
			result = other.convertTo(this);
			result.amount += amount;
		}
		return result;			 
	}

	public Unit convertTo(Unit other) {
		Unit result = new Unit(other);
		result.amount = amount * ((double)factor/other.factor);
		return result;
	}

	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("#.#");
		return String.format("%s%s", df.format(amount), id);
	}
}
