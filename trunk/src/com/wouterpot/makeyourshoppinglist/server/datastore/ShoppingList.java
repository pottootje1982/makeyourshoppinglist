package com.wouterpot.makeyourshoppinglist.server.datastore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.jdo.PersistenceManager;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
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
	
    @NotPersistent
	Map<String, ArrayList<Product>> categoriesToProducts = new HashMap<String, ArrayList<Product>>();
    
    @Persistent(mappedBy = "shoppingList")
    @Element(dependent = "true")
	ArrayList<Product> products = new ArrayList<Product>();
    
    @Persistent
    ArrayList<String> sites = new ArrayList<String>();
        
	private LanguageDictionary languageDictionary;

	public ShoppingList(LanguageDictionary languageDictionary) {
		this.languageDictionary = languageDictionary;
	}

	public void addIngredients(String recipeId, List<String> ingredients, String language) {
		if (!sites.contains(recipeId)) {
			CategoryDictionary categoryDictionary = languageDictionary.getCategoryDictionary(language);
			for (String ingredient : ingredients) {
				Product product = categoryDictionary.getProduct(ingredient);
				if (product == null) product = new Product(ingredient);
				addProduct(product);
			}
			sites.add(recipeId);
		}
	}

	private void addProduct(Product product) {
		String category = product.getCategory();
		ArrayList<Product> products = categoriesToProducts.get(category);
		if (products == null) {
			products = new ArrayList<Product>();
			categoriesToProducts.put(category, products);
		}
		products.add(product);
		this.products.add(product);
		persistProduct(product);
	}

	private void persistProduct(Product product) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ShoppingList shoppingList = pm.makePersistent(this);
		shoppingList.products.add(product);
		pm.close();
	}
	
	private String[] getCategories() {
		Set<String> keySet = categoriesToProducts.keySet();
		String[] categories = new String[keySet.size()];
		keySet.toArray(categories);
		Arrays.sort(categories);
		return categories;
	}

	public List<Product> getProducts(String category) {
		return categoriesToProducts.get(category);
	}
	
	public ArrayList<Product> getProducts() {
		return products;
	}
	public Map<String, ArrayList<String>> getShoppingList() {
		Map<String, ArrayList<String>> result = new TreeMap<>();
		String[] categories = getCategories();
		for (String category : categories) {
			ArrayList<String> productStrings = new ArrayList<String>();
			result.put(category, productStrings);
			List<Product> products = getProducts(category);
			for (Product product : products) {
				productStrings.add(product.getIngredient());
			}			
		}
		return result;
	}

	public boolean isEmpty() {
		return categoriesToProducts.isEmpty();
	}
}
