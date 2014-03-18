package com.wouterpot.makeyourshoppinglist;

public class Product {

	private String productKey;
	private boolean isExcluded;
	private boolean isCommon;

	public Product(String productKey, boolean isExcluded, boolean isCommon) {
		this.productKey = productKey;
		this.setExcluded(isExcluded);
		this.isCommon = isCommon;
	}

	public String getProductKey() {
		return productKey;
	}

	public boolean isExcluded() {
		return isExcluded;
	}

	private void setExcluded(boolean isExcluded) {
		this.isExcluded = isExcluded;
	}
}
