package com.wouterpot.makeyourshoppinglist.server.datastore;

public enum UnitType {
	m(1000),
	dm(100),
	cm(10),
	mm(1),
	l(1000),
	dl(100),
	cl(10),
	ml(1),
	kg(1000),
	g(1),
	tbsp,
	ts,
	el,
	tl, 
	splashes(true), 
	drops(true),
	glas(true),
	glass(true),
	rashers(true),
	
	pieces,
	NaN;
	
	private String value;
	private int factor;
	private boolean addSpace;
	
	UnitType() {
		this(0);
		value = toString();
	}
	
	UnitType(int factor) {
		this.factor = factor;
	}
	
	UnitType(boolean addSpace) {
		this.addSpace = addSpace;		
	}

	public int getFactor() {
		return factor;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		String string = super.toString();
		return addSpace ? " " + string : string;
	}
	
	public static UnitType parse(String value) {
		switch (value.toLowerCase().replaceAll("\\.", "")) {
		case "mm": return mm;
		case "cm": return cm;
		case "dm": return dm;
		case "m": return m;
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
		case "glass": return glass;
		case "glas": return glas;
		case "rashers": return rashers;
		case "number": return pieces;
		case "nan": return NaN;
		}
		return null;
	}
}
