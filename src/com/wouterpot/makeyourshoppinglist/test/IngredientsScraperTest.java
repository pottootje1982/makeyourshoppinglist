package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.wouterpot.makeyourshoppinglist.IngredientsList;
import com.wouterpot.makeyourshoppinglist.IngredientsScraper;

public class IngredientsScraperTest {

	private IngredientsScraper ingredientsScraper;

	@Before
	public void setup()
	{
		ingredientsScraper = new IngredientsScraper();

	}
	
	@Test
	public void test() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		org.w3c.dom.Document doc = dBuilder.parse("res/ingredientsClasses.xml");
	}
	
	@Test
	public void testSiteWithItemList() throws MalformedURLException, IOException {
		String url = "https://sites.google.com/site/walterreddock/recepten/zeebrasemfilets-met-kleine-groentes";
		IngredientsList ingredientsList = ingredientsScraper.getIngredients(url);
		ArrayList<String> ingredients = ingredientsList.getIngredients();
		assertEquals(10, ingredients.size());
		assertEquals("3 bospeentjes", ingredients.get(0));
		assertEquals("4 dauradefilets (of een andere vis)", ingredients.get(9));
	}

}
