package com.wouterpot.makeyourshoppinglist;

import com.wouterpot.makeyourshoppinglist.config.ProductInfo;

public class Product {

	private static final String DEFAULT_CATEGORY = "supermarket";
	private String ingredient;
	private ProductInfo productInfo;

	public Product(String ingredient, ProductInfo productInfo) {
		this.ingredient = ingredient;
		this.productInfo = productInfo;
	}

	public Product(String ingredient) {
		this(ingredient, null);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (ingredient == null) {
			if (other.ingredient != null)
				return false;
		} else if (!ingredient.equals(other.ingredient))
			return false;
		return true;
	}

	public String getIngredient() {
		return ingredient;		
	}

	public String getCategory() {
		return productInfo != null ? productInfo.getCategory() : DEFAULT_CATEGORY;
	}

	@Override
	public String toString() {
		return "Product [ingredient=" + ingredient + ", productInfo="
				+ productInfo + "]";
	}

}
