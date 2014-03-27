package com.wouterpot.makeyourshoppinglist.server.datastore;

public enum UnitType {
	l(1000),
	dl(100),
	cl(10),
	ml(1),
	kg(1000),
	g(1),
	tbsp,
	ts,
	el,
	tl, splashes, drops;
	
	private int factor;
	
	UnitType() {
		this(0);
	}
	
	UnitType(int factor) {
		this.factor = factor;
	}

	public int getFactor() {
		return factor;
	}
	
	public static UnitType parse(String value) {
		switch (value.toLowerCase().replaceAll("\\.", "")) {
		case "liter":
		case "l": return l;
		case "dl": return dl;
		case "cl": return cl;
		case "ml": return ml;
		case "kg": return kg;
		case "g": 
		case "gram":
		case "grammes": return g;
		case "tbsp": return tbsp;
		case "tsp":
		case "ts": return ts;
		case "eetlepel": 
		case "el": return el;
		case "theelepel":
		case "tl": return tl;
		}
		return null;
	}
}
