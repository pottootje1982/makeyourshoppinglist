package com.wouterpot.makeyourshoppinglist.server;

import java.io.File;
import java.util.List;

import com.wouterpot.makeyourshoppinglist.config.LanguageDictionary;
import com.wouterpot.makeyourshoppinglist.server.datastore.ShoppingList;

public class ShoppingListFactory {

	private static final ShoppingListFactory shoppingListFactoryInstance = new ShoppingListFactory();
	
	private IngredientsScraper ingredientsScraper;
	private LanguageDictionary languageDictionary;
	private ShoppingList shoppingList = null;

	private ShoppingListFactory() {
		ingredientsScraper = new IngredientsScraper();
		languageDictionary = new LanguageDictionary("data");
	}

	public static ShoppingListFactory get() {
		return shoppingListFactoryInstance;
	}
	
	public void setShoppingList(ShoppingList shoppingList) {
		shoppingList.setLanguageDictionary(languageDictionary);
		this.shoppingList = shoppingList;
	}

	public ShoppingList getShoppingList() {
		if (shoppingList == null) {
			shoppingList = new ShoppingList(languageDictionary);
		}
		return shoppingList;
	}
	
	public ShoppingList createNewShoppingList() {
		shoppingList = null;
		ShoppingList newShoppingList = getShoppingList();
		return newShoppingList;
	}

	private void addToShoppingList(String recipeId, IngredientsList ingredientsList) {
		List<String> ingredients = ingredientsList.getIngredients();
		String language = ingredientsList.getSiteInfo().getLanguage();
		getShoppingList().addIngredients(recipeId, ingredients, language);
	}

	public void addToShoppingList(File file) {
		try {
			IngredientsList ingredientsList = ingredientsScraper.getIngredients(file);
			addToShoppingList(file.getAbsolutePath(), ingredientsList);
		} catch (IngredientsScraperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addToShoppingList(String url) {
		try {
			IngredientsList ingredientsList = ingredientsScraper.getIngredients(url);
			addToShoppingList(url, ingredientsList);
		} catch (IngredientsScraperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
