package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

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

	// TODO: sort out how this works
	private void mockPMF() {
		Ingredient temp = new Ingredient("");
		PowerMock.mockStatic(PMF.class);
		//Mockito.when(PMF.makePersistent(temp)).thenReturn(temp);
	}
	
	@Test
	public void quantityNumber() {
		Ingredient ingredient = new Ingredient("2 el roomboter");
		Unit unit = ingredient.getUnit(UnitType.el);
		assertEquals("2el", unit.toString());
		assertEquals("roomboter", ingredient.getProductName());
	}
	
	@Test
	public void quantityDividedNumber() {
		Ingredient ingredient = new Ingredient("3.5 l melk");
		Unit unit = ingredient.getUnit(QuantityType.Volume);
		assertEquals("3.5l", unit.toString());
		assertEquals("melk", ingredient.getProductName());
	}

	@Test
	public void alternativeUnitName() {
		Ingredient ingredient = new Ingredient("3.5 liter melk");
		Unit unit = ingredient.getUnit(QuantityType.Volume);
		assertEquals("3.5l", unit.toString());
		assertEquals("melk", ingredient.getProductName());
	}
	
	@Test
	public void quantityUnitWithInterpunction() {
		Ingredient ingredient = new Ingredient("3.5 l. melk");
		Unit unit = ingredient.getUnit(QuantityType.Volume);
		assertEquals("3.5l", unit.toString());
		assertEquals("melk", ingredient.getProductName());
	}

	@Test
	public void quantityNoUnit() {
		Ingredient ingredient = new Ingredient("2 kaneelstokjes");
		Unit unit = ingredient.getUnit(QuantityType.Countable);
		assertEquals("2", unit.toString());
		assertEquals("kaneelstokjes", ingredient.getProductName());
	}
	
	// TODO: 1/2 glas witte wijn
	// glas should be unit
	
	// TODO: 2-3 rashers streaky bacon
	
	// TODO: 50ml olive oil, for frying & extra-virgin olive oil,	for drizzling
	// shouldn't be added
	
	@Test
	public void caseInsensitiveUnit() {
		Ingredient ingredient = new Ingredient("2 Tl kaneel");
		Unit unit = ingredient.getUnit(UnitType.tl);
		assertEquals("2tl", unit.toString());
		assertEquals("kaneel", ingredient.getProductName());
	}
	
	@Test
	public void quantityNoUnitIngredientContainingMultipleWords() {
		Ingredient ingredient = new Ingredient("2 gele paprikas");
		Unit unit = ingredient.getUnit(QuantityType.Countable);
		assertEquals("2", unit.toString());
		assertEquals("gele paprikas", ingredient.getProductName());
	}
	
	@Test
	public void noQuantity() {
		Ingredient ingredient = new Ingredient("handful flat-leaf parsley, roughly chopped");
		Unit unit = ingredient.getUnit(QuantityType.Uncountable);
		assertEquals("", unit.toString());
		assertEquals("handful flat-leaf parsley, roughly chopped", ingredient.getProductName());
		assertEquals("handful flat-leaf parsley, roughly chopped", ingredient.toString());
	}
	
	@Test
	public void testAddSameType() {
		Ingredient ingredient1 = new Ingredient("2 gele paprikas");
		Ingredient ingredient2 = new Ingredient("3 gele paprikas");
		ingredient1.add(ingredient2);
		assertEquals("5 gele paprikas", ingredient1.toString());
	}
	
	@Test
	public void testAddDifferentType() {
		Ingredient ingredient1 = new Ingredient("2 el olie");
		Ingredient ingredient2 = new Ingredient("20 ml olie");
		ingredient1.add(ingredient2);
		assertEquals("2el + 20ml olie", ingredient1.toString());
	}
}
