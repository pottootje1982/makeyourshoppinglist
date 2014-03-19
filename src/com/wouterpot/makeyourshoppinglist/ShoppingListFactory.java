package com.wouterpot.makeyourshoppinglist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.wouterpot.makeyourshoppinglist.config.CategoryDictionary;
import com.wouterpot.makeyourshoppinglist.config.LanguageDictionary;

public class ShoppingListFactory {

	private IngredientsScraper ingredientsScraper;
	private LanguageDictionary languageDictionary;

	public ShoppingListFactory() {
		ingredientsScraper = new IngredientsScraper();
		languageDictionary = new LanguageDictionary("data");
	}

	public ShoppingList createShoppingList(File file, String language) {
		ArrayList<String> ingredients;
		try {
			ingredients = ingredientsScraper.getIngredients(file).getIngredients();
			CategoryDictionary categoryDictionary = languageDictionary.getCategoryDictionary(language);
			ShoppingList shoppingList = new ShoppingList(categoryDictionary, ingredients);
			return shoppingList;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
