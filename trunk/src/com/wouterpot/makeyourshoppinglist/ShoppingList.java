package com.wouterpot.makeyourshoppinglist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.wouterpot.makeyourshoppinglist.config.CategoryDictionary;
import com.wouterpot.makeyourshoppinglist.config.LanguageDictionary;

public class ShoppingList {
	Map<String, ArrayList<Product>> categoriesToProducts = new HashMap<String, ArrayList<Product>>();
	private LanguageDictionary languageDictionary;

	public ShoppingList(LanguageDictionary languageDictionary) {
		this.languageDictionary = languageDictionary;
	}

	public void addIngredients(ArrayList<String> ingredients, String language) {
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
			products = new ArrayList<>();
			categoriesToProducts.put(category, products);
		}
		products.add(product);
	}
	
	public String[] getCategories() {
		Set<String> keySet = categoriesToProducts.keySet();
		String[] categories = new String[keySet.size()];
		keySet.toArray(categories);
		Arrays.sort(categories);
		return categories;
	}

	public List<Product> getProducts(String category) {
		return categoriesToProducts.get(category);
	}

	public boolean isEmpty() {
		return categoriesToProducts.isEmpty();
	}
}
