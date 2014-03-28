package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import com.wouterpot.makeyourshoppinglist.server.PMF;
import com.wouterpot.makeyourshoppinglist.server.datastore.Ingredient;
import com.wouterpot.makeyourshoppinglist.server.datastore.Unit;
import com.wouterpot.makeyourshoppinglist.server.datastore.QuantityType;
import com.wouterpot.makeyourshoppinglist.server.datastore.UnitType;

//@RunWith(PowerMockRunner.class) // TODO: sort out how this works
@PrepareForTest(PMF.class)
public class IngredientTest extends DataStoreTestBase {

	@BeforeClass
	public static void setup()	{
		PMF.setTesting(true);
	}
	
	@AfterClass
	public static void teardown() {
		PMF.setTesting(false);
	}
	
	// TODO: sort out how this works
	private void mockPMF() {
		Ingredient temp = new Ingredient();
		PowerMock.mockStatic(PMF.class);
		//Mockito.when(PMF.makePersistent(temp)).thenReturn(temp);
	}
	
	private Ingredient createIngredient(String ingredientName) {
		Ingredient ingredient = new Ingredient();
		ingredient.parseIngredient(ingredientName);
		return ingredient;
	}

	@Test
	public void quantityNumber() {
		Ingredient ingredient = createIngredient("2 el roomboter");
		Unit unit = ingredient.getUnit(UnitType.el);
		assertEquals("2el", unit.toString());
		assertEquals("roomboter", ingredient.getProductName());
	}
	
	@Test
	public void quantityDividedNumber() {
		Ingredient ingredient = createIngredient("3.5 l melk");
		Unit unit = ingredient.getUnit(QuantityType.Volume);
		assertEquals("3.5l", unit.toString());
		assertEquals("melk", ingredient.getProductName());
	}

	@Test
	public void alternativeUnitName() {
		Ingredient ingredient = createIngredient("3.5 liter melk");
		Unit unit = ingredient.getUnit(QuantityType.Volume);
		assertEquals("3.5l", unit.toString());
		assertEquals("melk", ingredient.getProductName());
	}
	
	@Test
	public void quantityUnitWithInterpunction() {
		Ingredient ingredient = createIngredient("3.5 l. melk");
		Unit unit = ingredient.getUnit(QuantityType.Volume);
		assertEquals("3.5l", unit.toString());
		assertEquals("melk", ingredient.getProductName());
	}

	@Test
	public void quantityNoUnit() {
		Ingredient ingredient = createIngredient("2 kaneelstokjes");
		Unit unit = ingredient.getUnit(QuantityType.Countable);
		assertEquals("2", unit.toString());
		assertEquals("kaneelstokjes", ingredient.getProductName());
	}
	
	// TODO: 1/2 glas witte wijn
	// glas should be unit
	
	@Test
	public void caseInsensitiveUnit() {
		Ingredient ingredient = createIngredient("2 Tl kaneel");
		Unit unit = ingredient.getUnit(UnitType.tl);
		assertEquals("2tl", unit.toString());
		assertEquals("kaneel", ingredient.getProductName());
	}
	
	@Test
	public void quantityNoUnitIngredientContainingMultipleWords() {
		Ingredient ingredient = createIngredient("2 gele paprikas");
		Unit unit = ingredient.getUnit(QuantityType.Countable);
		assertEquals("2", unit.toString());
		assertEquals("gele paprikas", ingredient.getProductName());
	}
	
	@Test
	public void noQuantity() {
		Ingredient ingredient = createIngredient("handful flat-leaf parsley, roughly chopped");
		Unit unit = ingredient.getUnit(QuantityType.Uncountable);
		assertEquals("", unit.toString());
		assertEquals("handful flat-leaf parsley, roughly chopped", ingredient.getProductName());
		assertEquals("handful flat-leaf parsley, roughly chopped", ingredient.toString());
	}
	
	@Test
	public void testAddSameType() {
		Ingredient ingredient1 = createIngredient("2 gele paprikas");
		Ingredient ingredient2 = createIngredient("3 gele paprikas");
		ingredient1.add(ingredient2);
		assertEquals("5 gele paprikas", ingredient1.toString());
	}
	
	@Test
	public void testAddDifferentType() {
		Ingredient ingredient1 = createIngredient("2 el olie");
		Ingredient ingredient2 = createIngredient("20 ml olie");
		ingredient1.add(ingredient2);
		assertEquals("2el + 20ml olie", ingredient1.toString());
	}
}
