package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.server.IngredientsList;
import com.wouterpot.makeyourshoppinglist.server.IngredientsScraper;
import com.wouterpot.makeyourshoppinglist.server.IngredientsScraperException;

public class IngredientsScraperTest {

	private IngredientsScraper ingredientsScraper;

	@Before
	public void setup()
	{
		ingredientsScraper = new IngredientsScraper();

	}
	
	@Test
	public void testSiteWithItemList() throws MalformedURLException, IOException, IngredientsScraperException {
		String url = "https://sites.google.com/site/walterreddock/recepten/zeebrasemfilets-met-kleine-groentes";
		IngredientsList ingredientsList = ingredientsScraper.getIngredients(url);
		List<String> ingredients = ingredientsList.getIngredients();
		assertEquals(10, ingredients.size());
		assertEquals("3 bospeentjes", ingredients.get(0));
		assertEquals("4 dauradefilets (of een andere vis)", ingredients.get(9));
	}

}
