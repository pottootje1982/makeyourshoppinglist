package com.wouterpot.makeyourshoppinglist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wouterpot.makeyourshoppinglist.config.CategoryDictionary;

public class ShoppingList {
	Map<String, ArrayList<Product>> categoriesToProducts = new HashMap<String, ArrayList<Product>>();

	public ShoppingList(CategoryDictionary categoryDictionary, ArrayList<String> ingredients) {
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

	public List<Product> getShoppingItems(String category) {
		return categoriesToProducts.get(category);
	}
}
