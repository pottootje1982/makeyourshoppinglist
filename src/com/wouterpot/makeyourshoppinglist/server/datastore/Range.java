package com.wouterpot.makeyourshoppinglist.server.datastore;

public class Range {

	private double minimum;
	private double maximum;

	public double getMinimum() {
		return minimum;
	}

	public double getMaximum() {
		return maximum;
	}

	public Range(double minimum, double maximum) {
		this.minimum = minimum;
		this.maximum = maximum;
	}
	
	public double getSize() {
		return maximum - minimum;
	}

	public static Range between(double minimum, double maximum) {
		return new Range(minimum, maximum);
	}

}
