package com.wouterpot.makeyourshoppinglist;

import java.io.File;
import java.util.ArrayList;

import com.wouterpot.makeyourshoppinglist.config.CategoryDictionary;
import com.wouterpot.makeyourshoppinglist.config.LanguageDictionary;

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
		ArrayList<String> ingredients = ingredientsList.getIngredients();
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
