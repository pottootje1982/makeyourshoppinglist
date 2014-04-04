package com.wouterpot.makeyourshoppinglist.server.datastore;

import java.util.ArrayList;
import java.util.List;
import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.labs.repackaged.com.google.common.base.Strings;
import com.google.gwt.thirdparty.guava.common.base.Joiner;
import com.wouterpot.makeyourshoppinglist.config.ProductInfo;
import com.wouterpot.makeyourshoppinglist.server.PMF;

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

	@Persistent(defaultFetchGroup = "true")
	@Embedded
	private ProductInfo productInfo;
	
    @Persistent
    private Category parent;
    
    @Persistent(defaultFetchGroup = "true")
    @Embedded
    private Ingredient ingredient;

    @Persistent(defaultFetchGroup = "true")
	private Boolean visible;
    
    @NotPersistent
    private List<String> aggregatedIds = new ArrayList<String>();
    
    @NotPersistent
    private List<String> aggregatedProductNames = new ArrayList<String>();

    @Persistent
	private Boolean isCustom;

    public Product() {
    }
    
	public Product(ProductInfo productInfo) {
		this(productInfo, false);
	}
	
	public Product(ProductInfo productInfo, boolean isCustom) {
		this.isCustom = isCustom;
		this.ingredient = null;
		this.productInfo = productInfo;
	}
	
	public Product(Product product) {
		this.id = product.id;
		this.ingredient = new Ingredient(product.getIngredient());
		this.productInfo = product.productInfo;
		this.visible = product.visible;
		this.isCustom = product.isCustom;
		for (String id : product.aggregatedIds)
			aggregatedIds.add(id);
		aggregatedProductNames.add(product.getIngredient().toString());
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
		if (getIngredient() == null) {
			if (other.getIngredient() != null)
				return false;
		} else if (!getIngredient().equals(other.getIngredient()))
			return false;
		return true;
	}

	public Boolean isCustom() {
		return isCustom;
	}

	private Ingredient getIngredient() {
		if (ingredient == null)
			PMF.retrieve(this);
		return ingredient;
	}

	public String getCategoryName() {
		return productInfo != null ? productInfo.getCategory() : DEFAULT_CATEGORY;
	}
	
	public ProductInfo getProductInfo() {
		return productInfo;
	}
	
	public String getProductKey() {
		if (productInfo != null) {
			String productKey = productInfo.getProductKey();
			if (!Strings.isNullOrEmpty(productKey) && !productInfo.isCommon()) return productKey;			
		}
		return ingredient.getProductName();
	}

	public List<String> getIds() {
		List<String> result = new ArrayList<>(aggregatedIds);
		result.add(id);
		return result;
	}
	
	public String getAggregatedProductName() {
		return aggregatedProductNames.size() > 1 ? Joiner.on("\n").join(aggregatedProductNames) : null;
	}

	@Override
	public String toString() {
		return getIngredient() != null ? getIngredient().toString() : "";
	}

	public Product add(Product product) {
		Product sum = new Product(this);
		if (product != null) {
			sum.getIngredient().add(product.getIngredient());
			sum.aggregatedIds.add(product.id);
			sum.aggregatedProductNames.add(product.getIngredient().toString());
		}
		return sum;
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

	public String getId() {
		return id;
	}

	public void setCategory(Category category) {
		this.parent = category;
	}
}
