package com.wouterpot.makeyourshoppinglist.server.datastore;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;

public enum QuantityType {
	Volume, Weight, Countable;
	
	private String value;
	
	private QuantityType() {
		this.value = this.toString();
	}
	
	public String getValue() {
		return value;
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
