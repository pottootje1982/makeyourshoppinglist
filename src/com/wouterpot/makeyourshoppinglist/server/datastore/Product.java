package com.wouterpot.makeyourshoppinglist.server.datastore;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.wouterpot.makeyourshoppinglist.config.ProductInfo;

@PersistenceCapable//(identityType = IdentityType.APPLICATION, detachable = "true")
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
    
    @Persistent
    @Embedded
    private Ingredient ingredient;

    @Persistent
	private Boolean visible = true;

	public Product(ProductInfo productInfo) {
		this.ingredient = null;
		this.productInfo = productInfo;
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

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return ingredient != null ? ingredient.toString() : "";
	}

	public void add(Product product) {
		ingredient.add(product.ingredient);
	}

	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;		
	}

	public Boolean getVisible() {
		return visible;
	}
}
