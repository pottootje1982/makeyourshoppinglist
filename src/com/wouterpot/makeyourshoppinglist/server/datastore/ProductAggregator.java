package com.wouterpot.makeyourshoppinglist.server.datastore;

import ch.lambdaj.function.aggregate.PairAggregator;

public class ProductAggregator extends PairAggregator<Product> {

	public ProductAggregator() {
	}

	@Override
	protected Product aggregate(Product arg0, Product arg1) {
		if (arg0 != null)
			return arg0.add(arg1);
		else
			return new Product(arg1);
	}

	@Override
	protected Product emptyItem() {
		return null;
	}

}
