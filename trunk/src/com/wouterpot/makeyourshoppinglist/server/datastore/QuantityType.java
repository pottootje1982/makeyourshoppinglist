package com.wouterpot.makeyourshoppinglist.server.datastore;

public enum QuantityType {
	Volume, Weight, Length, Countable, Uncountable;
	
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
		case "uncountable": return Uncountable;
		}
		return null;
	}
}
