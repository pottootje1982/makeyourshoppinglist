package com.wouterpot.makeyourshoppinglist.server.datastore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.labs.repackaged.com.google.common.base.Strings;
import com.wouterpot.makeyourshoppinglist.config.CategoryDictionary;
import com.wouterpot.makeyourshoppinglist.config.LanguageDictionary;
import com.wouterpot.makeyourshoppinglist.config.ProductInfo;
import com.wouterpot.makeyourshoppinglist.helpers.RegEx;
import com.wouterpot.makeyourshoppinglist.server.PMF;

@PersistenceCapable

public class ShoppingList {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String      id;
	
    @Persistent(mappedBy = "parent")
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
				createProduct(categoryDictionary, ingredient);
			}
			shoppingList.sites.add(recipeId);
			
			PMF.close();
		}
	}

	public void createProduct(CategoryDictionary categoryDictionary, String ingredientString) {
		ingredientString = RegEx.escapeStrangeChars(ingredientString);
		ingredientString = ingredientString.trim();
		if (Strings.isNullOrEmpty(ingredientString)) return;
		ProductInfo productInfo = categoryDictionary.getProductInfo(ingredientString);
		Category category = getOrCreateCategory(productInfo != null ? productInfo.getCategory() : Product.DEFAULT_CATEGORY);
		
		Product product = new Product(null, productInfo);
		Ingredient ingredient = new Ingredient(null);
		category.addProduct(product);
		ingredient.parseIngredient(ingredientString);
		product.setIngredient(ingredient);
	}
	
	private Category getCategory(String categoryName) {
		for (Category category : categoriesToProducts) {
			if (category.equals(categoryName))
				return category;
		}
		return null;
	}

	private Category getOrCreateCategory(String categoryName) {
		Category category = getCategory(categoryName);
		if (category == null) {
			category = new Category(categoryName);
			category.setParent(this);
			categoriesToProducts.add(category);
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
		PMF.open();
		
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
		PMF.close();
		return result;
	}

	public boolean isEmpty() {
		return categoriesToProducts.isEmpty();
	}
}