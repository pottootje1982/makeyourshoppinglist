package com.wouterpot.makeyourshoppinglist.config;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import com.wouterpot.makeyourshoppinglist.helpers.RegEx;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class ProductInfo {
    @PrimaryKey
	private String productKey;
	private String category;
	private boolean isExcluded;
	private boolean isCommon;

	public ProductInfo(String productKey, String category, boolean isExcluded, boolean isCommon) {
		this.productKey = productKey;
		this.category = category;
		this.setExcluded(isExcluded);
		this.isCommon = isCommon;
	}

	public ProductInfo(String productKey) {
		this(productKey, null, false, false);
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

	public String getCategory() {
		return category;
	}
	
	public boolean productMatches(String ingredient) {
		String matchString =  "(\\S*)(" + getProductKey() + "\\S*)";
		String[] matches = RegEx.findGroups(ingredient, matchString);
		if (matches.length > 0)
		{
			String spaces = matches[0];
			String match = matches[1];
			boolean productMatches = matches.length == 2 
					&& (double)getProductKey().length() / match.length() >= 0.5
					&& spaces.length() == 0;
			return productMatches;
		}
		else
			return false;
	}

	@Override
	public String toString() {
		return "ProductInfo [productKey=" + productKey + ", category="
				+ category + ", isExcluded=" + isExcluded + ", isCommon="
				+ isCommon + "]";
	}
	
	
}
