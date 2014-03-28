package com.wouterpot.makeyourshoppinglist.server.datastore;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.wouterpot.makeyourshoppinglist.config.ProductInfo;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Product {

	public static final String DEFAULT_CATEGORY = "supermarket";

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(
        vendorName = "datanucleus",
        key        = "gae.encoded-pk",
        value      = "true"
    )
    private String      id;

	@Persistent
	@Embedded
	private ProductInfo productInfo;
	
    @Persistent
    private Category parent;
    
    @Persistent(mappedBy = "product")
    @Embedded
    private Ingredient ingredient;

	public Product(Category category, ProductInfo productInfo) {
		this.parent = category;
		this.ingredient = new Ingredient(this);
		this.productInfo = productInfo;
	}

	public Product(ProductInfo productInfo) {
		this(null, productInfo);
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

	public String getCategoryName() {
		return productInfo != null ? productInfo.getCategory() : DEFAULT_CATEGORY;
	}
	
	public ProductInfo getProductInfo() {
		return productInfo;
	}

	@Override
	public String toString() {
		return ingredient.toString();
	}

	public void add(Product product) {
		ingredient.add(product.ingredient);
	}

	public void add(Ingredient ingredient) {
		this.ingredient.add(ingredient);
	}	
}
