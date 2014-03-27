package com.wouterpot.makeyourshoppinglist.server.datastore;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public enum QuantityType {
	Volume, Weight, Countable, MeasurableQuantity;

	private QuantityType() {

	}

	public static QuantityType parse(String value) {
		switch (value.toLowerCase()) {
		case "volume": return Volume;
		case "weight": return Weight;
		case "countable": return Countable;
		}
		return null;

	}
}
