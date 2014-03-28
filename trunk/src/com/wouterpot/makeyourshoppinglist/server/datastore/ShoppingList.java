package com.wouterpot.makeyourshoppinglist.server.datastore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.wouterpot.makeyourshoppinglist.config.CategoryDictionary;
import com.wouterpot.makeyourshoppinglist.config.LanguageDictionary;
import com.wouterpot.makeyourshoppinglist.server.PMF;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")

public class ShoppingList {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String      id;
	
    @Persistent(mappedBy = "shoppingList")
    @Element(dependent = "true")
    List<Category> categoriesToProducts;
    
    @Persistent
    ArrayList<String> sites = new ArrayList<String>();
        
	private LanguageDictionary languageDictionary;

	public ShoppingList(LanguageDictionary languageDictionary) {
		this.languageDictionary = languageDictionary;
		categoriesToProducts = new ArrayList<Category>();
	}

	public void addIngredients(String recipeId, List<String> ingredients, String language) {
		if (!sites.contains(recipeId)) {
			PMF.open();
			ShoppingList shoppingList = PMF.makePersistent(this);
			
			CategoryDictionary categoryDictionary = languageDictionary.getCategoryDictionary(language);
			for (String ingredient : ingredients) {
				Product product = categoryDictionary.getProduct(ingredient);
				Category category = getOrCreateCategory(product.getCategoryName());
				product.setCategory(category);
				category.addProduct(product);
			}
			shoppingList.sites.add(recipeId);
			
			PMF.close();
		}
	}

/*	private void persistCategoriesToProducts(List<Product> products) {
		PMF.open();
		ShoppingList shoppingList = pm.makePersistent(this);
		for (Category category : categoriesToProducts) {
			shoppingList.categoriesToProducts.add(category);
		}
		for (Product product : products) {
			Category category = getCategory(product.getCategoryName());
			category = PMF.makePersistent(category);
			category.addProduct(product);			
		}
		PMF.close();
	}*/
	
	private Category getCategory(String categoryName) {
		for (Category category : categoriesToProducts) {
			if (category.equals(categoryName))
				return category;
		}
		return null;
	}

	private Category getOrCreateCategory(String categoryName) {
		ShoppingList shoppingList = PMF.makePersistent(this);
		Category category = getCategory(categoryName);
		if (category == null) {
			category = new Category(categoryName);
			category.setShoppingList(this);
			shoppingList.categoriesToProducts.add(category);
		}
		return category;
	}

	public List<Category> getCategories() {
		Collections.sort(categoriesToProducts);
		return categoriesToProducts;
	}

	public List<Product> getProducts(String categoryName) {
		Category category = getCategory(categoryName);
		return category.getProducts();
	}
	
	public void setLanguageDictionary(LanguageDictionary languageDictionary) {
		this.languageDictionary = languageDictionary;
	}

	public Map<String, ArrayList<String>> getShoppingList() {
		Map<String, ArrayList<String>> result = new TreeMap<>();
		List<Category> categories = getCategories();
		for (Category category : categories) {
			ArrayList<String> productStrings = new ArrayList<String>();
			result.put(category.getCategoryName(), productStrings);
			List<Product> products = category.getProducts();			
			for (Product product : products) {
				productStrings.add(product.toString());
			}			
		}
		return result;
	}

	public boolean isEmpty() {
		return categoriesToProducts.isEmpty();
	}
}