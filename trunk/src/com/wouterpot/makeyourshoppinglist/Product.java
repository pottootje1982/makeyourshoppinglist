package com.wouterpot.makeyourshoppinglist;

public class Product {

	private String productKey;
	private boolean isExcluded;
	private boolean isCommon;

	public Product(String productKey, boolean isExcluded, boolean isCommon) {
		this.productKey = productKey;
		this.isExcluded = isExcluded;
		this.isCommon = isCommon;
	}

	public String getProductKey() {
		return productKey;
	}

}
