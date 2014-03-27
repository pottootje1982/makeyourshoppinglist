package com.wouterpot.makeyourshoppinglist.server.datastore;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.commons.lang3.StringUtils;

import com.wouterpot.makeyourshoppinglist.server.PMF;

@SuppressWarnings("unused")
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Category implements Comparable<Category> {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(
        vendorName = "datanucleus",
        key        = "gae.encoded-pk",
        value      = "true"
    )
    private String      id;
    
	@Persistent
	private String categoryName;
	
    @Persistent(mappedBy = "category")
    @Element(dependent = "true")
	private List<Product> products = new ArrayList<Product>();

    @Persistent
	private ShoppingList shoppingList;
	
	public Category(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public List<Product> getProducts() {
		if (products == null) {
			// Products can be null due to lazy evaluation
			PersistenceManager pm = PMF.get().getPersistenceManager();
			pm.currentTransaction().begin();
			Category category = pm.getObjectById(Category.class, id);
			if (category != null) {
				pm.retrieve(category);
				products = category.products;
			}
			pm.currentTransaction().commit();
			pm.close();
		}
		return products;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setShoppingList(ShoppingList shoppingList) {
		this.shoppingList = shoppingList;
	}

	public void addProduct(Product product) {
		product.setCategory(this);
		products.add(product);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((categoryName == null) ? 0 : categoryName.hashCode());
		result = prime * result
				+ ((products == null) ? 0 : products.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (categoryName == null) {
			if (other.categoryName != null)
				return false;
		} else if (!categoryName.equals(other.categoryName))
			return false;

		return true;
	}
	
	public boolean equals(String categoryName) {
		return this.categoryName.equals(categoryName);
	}

	@Override
	public int compareTo(Category category) {
		if (category == null)
			return -1;
		return categoryName.compareTo(category.categoryName);
	}

	@Override
	public String toString() {
		return "Category [categoryName=" + categoryName
				+ ", products=" + StringUtils.join(products, '\n') + "]";
	}

	public void clearProducts() {
		products.clear();		
	}
}
