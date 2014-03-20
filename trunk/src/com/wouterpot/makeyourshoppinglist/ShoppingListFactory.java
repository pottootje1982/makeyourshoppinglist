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

	private ShoppingList createShoppingList(IngredientsList ingredientsList) {
		ArrayList<String> ingredients = ingredientsList.getIngredients();
		String language = ingredientsList.getSiteInfo().getLanguage();
		CategoryDictionary categoryDictionary = languageDictionary.getCategoryDictionary(language);
		ShoppingList shoppingList = new ShoppingList(categoryDictionary, ingredients);
		return shoppingList;
	}

	public ShoppingList createShoppingList(File file) {
		try {
			IngredientsList ingredientsList = ingredientsScraper.getIngredients(file);
			return createShoppingList(ingredientsList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ShoppingList createShoppingList(String url) {
		try {
			IngredientsList ingredientsList = ingredientsScraper.getIngredients(url);
			return createShoppingList(ingredientsList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;		
	}

}
