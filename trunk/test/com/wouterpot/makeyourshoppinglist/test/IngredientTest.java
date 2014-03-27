package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.server.datastore.Ingredient;
import com.wouterpot.makeyourshoppinglist.server.datastore.Quantity;

public class IngredientTest {

	@Test
	public void quantityNumber() {
		Ingredient ingredient = new Ingredient("2 el roomboter");
		Quantity quantity = ingredient.getQuantity("el");
		assertEquals("2 el", quantity.toString());
		assertEquals("roomboter", ingredient.getProductName());
	}
	
	@Test
	public void quantityDividedNumber() {
		Ingredient ingredient = new Ingredient("3.5 liter melk");
		assertEquals(3.5, ingredient.getAmount(), 0);
		assertEquals("liter", ingredient.getUnit());
		assertEquals("melk", ingredient.getProductName());
	}

	@Test
	public void quantityNoUnit() {
		Ingredient ingredient = new Ingredient("2 kaneelstokjes");
		assertEquals(2, ingredient.getAmount(), 0);
		assertNull(ingredient.getUnit());
		assertEquals("kaneelstokjes", ingredient.getProductName());
	}
	
	@Test
	public void quantityNoUnitIngredientContainingMultipleWords() {
		Ingredient ingredient = new Ingredient("2 gele paprikas");
		assertEquals(2, ingredient.getAmount(), 0);
		assertNull(ingredient.getUnit());
		assertEquals("gele paprikas", ingredient.getProductName());
	}
}
