package com.wouterpot.makeyourshoppinglist.server.datastore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
	Set<Product> products = new HashSet<Product>();
        
	private LanguageDictionary languageDictionary;

	public ShoppingList(LanguageDictionary languageDictionary) {
		this.languageDictionary = languageDictionary;
	}

	public void addIngredients(List<String> ingredients, String language) {
		CategoryDictionary categoryDictionary = languageDictionary.getCategoryDictionary(language);
		for (String ingredient : ingredients) {
			Product product = categoryDictionary.getProduct(ingredient);
			if (product == null) product = new Product(ingredient);
			put(product);
		}
	}

	private void put(Product product) {
		String category = product.getCategory();
		ArrayList<Product> products = categoriesToProducts.get(category);
		if (products == null) {
			products = new ArrayList<Product>();
			categoriesToProducts.put(category, products);
		}
		products.add(product);
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
