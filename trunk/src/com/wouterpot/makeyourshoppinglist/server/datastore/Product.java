package com.wouterpot.makeyourshoppinglist.server.datastore;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.wouterpot.makeyourshoppinglist.config.ProductInfo;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")

public class Product {

	private static final String DEFAULT_CATEGORY = "supermarket";

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(
        vendorName = "datanucleus",
        key        = "gae.encoded-pk",
        value      = "true"
    )
    private String      id;

    @Persistent
	private String ingredient;

	@Persistent
	private ProductInfo productInfo;
	
    @SuppressWarnings("unused")
    @Persistent
    private ShoppingList shoppingList;

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
