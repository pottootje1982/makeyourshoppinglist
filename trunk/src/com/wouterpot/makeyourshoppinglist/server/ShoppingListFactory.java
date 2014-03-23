package com.wouterpot.makeyourshoppinglist.server;

import java.io.File;
import java.util.List;

import com.wouterpot.makeyourshoppinglist.config.LanguageDictionary;
import com.wouterpot.makeyourshoppinglist.server.datastore.ShoppingList;

public class ShoppingListFactory {

	private IngredientsScraper ingredientsScraper;
	private LanguageDictionary languageDictionary;
	private ShoppingList shoppingList;

	public ShoppingList getShoppingList() {
		return shoppingList;
	}

	public ShoppingListFactory() {
		ingredientsScraper = new IngredientsScraper();
		languageDictionary = new LanguageDictionary("data");
		shoppingList = new ShoppingList(languageDictionary);
	}

	private void addToShoppingList(IngredientsList ingredientsList) {
		List<String> ingredients = ingredientsList.getIngredients();
		String language = ingredientsList.getSiteInfo().getLanguage();
		shoppingList.addIngredients(ingredients, language);
	}

	public void addToShoppingList(File file) {
		try {
			IngredientsList ingredientsList = ingredientsScraper.getIngredients(file);
			addToShoppingList(ingredientsList);
		} catch (IngredientsScraperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addToShoppingList(String url) {
		try {
			IngredientsList ingredientsList = ingredientsScraper.getIngredients(url);
			addToShoppingList(ingredientsList);
		} catch (IngredientsScraperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
