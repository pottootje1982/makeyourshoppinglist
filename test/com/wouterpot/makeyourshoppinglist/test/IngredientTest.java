package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.server.datastore.Ingredient;
import com.wouterpot.makeyourshoppinglist.server.datastore.Quantity;
import com.wouterpot.makeyourshoppinglist.server.datastore.QuantityType;
import com.wouterpot.makeyourshoppinglist.server.datastore.UnitType;

public class IngredientTest {

	@Test
	public void quantityNumber() {
		Ingredient ingredient = new Ingredient("2 el roomboter");
		Quantity quantity = ingredient.getQuantity(UnitType.el);
		assertEquals("2el", quantity.toString());
		assertEquals("roomboter", ingredient.getProductName());
	}
	
	@Test
	public void quantityDividedNumber() {
		Ingredient ingredient = new Ingredient("3.5 l melk");
		Quantity quantity = ingredient.getQuantity(QuantityType.Volume);
		assertEquals("3.5l", quantity.toString());
		assertEquals("melk", ingredient.getProductName());
	}

	@Test
	public void alternativeUnitName() {
		Ingredient ingredient = new Ingredient("3.5 liter melk");
		Quantity quantity = ingredient.getQuantity(QuantityType.Volume);
		assertEquals("3.5l", quantity.toString());
		assertEquals("melk", ingredient.getProductName());
	}
	
	@Test
	public void quantityUnitWithInterpunction() {
		Ingredient ingredient = new Ingredient("3.5 l. melk");
		Quantity quantity = ingredient.getQuantity(QuantityType.Volume);
		assertEquals("3.5l", quantity.toString());
		assertEquals("melk", ingredient.getProductName());
	}

	@Test
	public void quantityNoUnit() {
		Ingredient ingredient = new Ingredient("2 kaneelstokjes");
		Quantity quantity = ingredient.getQuantity(QuantityType.Countable);
		assertEquals("2", quantity.toString());
		assertEquals("kaneelstokjes", ingredient.getProductName());
	}
	
	@Test
	public void caseInsensitiveUnit() {
		Ingredient ingredient = new Ingredient("2 Tl kaneel");
		Quantity quantity = ingredient.getQuantity(UnitType.tl);
		assertEquals("2tl", quantity.toString());
		assertEquals("kaneel", ingredient.getProductName());
	}
	
	@Test
	public void quantityNoUnitIngredientContainingMultipleWords() {
		Ingredient ingredient = new Ingredient("2 gele paprikas");
		Quantity quantity = ingredient.getQuantity(QuantityType.Countable);
		assertEquals("2", quantity.toString());
		assertEquals("gele paprikas", ingredient.getProductName());
	}
}
