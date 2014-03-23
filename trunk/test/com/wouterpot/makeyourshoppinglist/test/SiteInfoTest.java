package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import java.util.List;

import com.wouterpot.makeyourshoppinglist.config.SiteInfo;
import com.wouterpot.makeyourshoppinglist.server.IngredientsList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

public class SiteInfoTest {

	@Test
	public void testList() throws Exception {
		String itemList = "<ul><li>3 bospeentjes</li><li>1 meiknolletje</li><li>100 gram peultjes</li><li>1 kleine courgette</li><li>2 sjalotten, gesnipperd</li><li>3 eetlepels roomboter</li><li>1/2 glas witte wijn</li><li>125 ml slagroom</li><li>125 ml cr�me fra�che</li><li>4 dauradefilets (of een andere vis)</li></ul>";

		Document doc = Jsoup.parse(itemList);
		Elements elements = doc.getElementsByTag("ul");
		SiteInfo siteInfo = new SiteInfo("nl", "ul", null, "sites-canvas-main-content", "li", null, null);
		IngredientsList ingredientsList = siteInfo.createIngredientsList(elements);
		List<String> ingredients = ingredientsList.getIngredients();
		assertEquals(10, ingredients.size());
		assertEquals("3 bospeentjes", ingredients.get(0));
		assertEquals("4 dauradefilets (of een andere vis)", ingredients.get(9));
	}
	
	@Test
	public void testBlob() {
		String blob = "<p>3 middelgrote courgettes<br>1 kleine ui<br>olijfolie<br>250 gr gepelde garnalen<br>1 groot blik gepelde tomaten<br>verse basilicum<br>flinke teen knoflook<br>4 ons schelpjespasta<br>vers gehakte peterselie<br>zout en peper</a></p>";
		Document doc = Jsoup.parse(blob);
		Elements elements = doc.getElementsByTag("p");
		SiteInfo siteInfo = new SiteInfo();
		IngredientsList ingredientsList = siteInfo.createIngredientsList(elements);
		List<String> ingredients = ingredientsList.getIngredients();
		assertEquals(10, ingredients.size());
		assertEquals("3 middelgrote courgettes", ingredients.get(0));
		assertEquals("zout en peper", ingredients.get(9));
	}

}
