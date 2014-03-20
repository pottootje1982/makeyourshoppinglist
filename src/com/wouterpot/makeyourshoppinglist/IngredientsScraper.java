package com.wouterpot.makeyourshoppinglist;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.google.appengine.labs.repackaged.com.google.common.io.Resources;
import com.wouterpot.makeyourshoppinglist.config.SiteInfo;
import com.wouterpot.makeyourshoppinglist.helpers.Resource;

public class IngredientsScraper {
	List<SiteInfo> siteInfos = new ArrayList<>();
	
	public IngredientsScraper()
	{
		getIngredientsClasses(Resource.getResource("config/ingredientsClasses.xml"));
	}
	
	private void getIngredientsClasses(String url)
	{
		try {
			File file = new File(url);
			Document doc = Jsoup.parse(file, "UTF-8");
			Element sites = doc.getElementsByTag("sites").get(0);
			List<Element> elements = sites.children();
			for (int i = 0; i < elements.size(); i++) {
				Node child = elements.get(i);
				Attributes attributes = child.attributes();
				if (attributes != null)
					siteInfos.add(new SiteInfo(attributes));
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private IngredientsList getIngredients(Document document) {
	    for (SiteInfo siteInfo : siteInfos) { 
	    	if (!siteInfo.matchesSite(document.baseUri())) continue; 
	    	
	        Elements ingredients = new Elements();
	        if (siteInfo.getClassName() != null) { 
	        	ingredients = document.getElementsByClass(siteInfo.getClassName());
	        } 
	        else if (siteInfo.getId() != null)
	        {
	        	Element element = document.getElementById(siteInfo.getId());
	        	if (element != null)
	        		ingredients.add(element);
	        }
	        String tagName = siteInfo.getTagName();
			if (tagName != null) { 
	        	ingredients = ingredients != null && ingredients.size() != 0 ? ingredients.get(0).getElementsByTag(tagName) : document.getElementsByTag(tagName); 
	        } 
	        if (ingredients.size() > 0) { 
	          return siteInfo.createIngredientsList(ingredients); 
	        } 
	    }
	    return null;
	}

	public IngredientsList getIngredients(File file) throws IOException {
		Document document = Jsoup.parse(file, "UTF-8");
		return getIngredients(document);
	}

	public IngredientsList getIngredients(String url) throws IOException {
		Document document = Jsoup.connect(url).get();
		return getIngredients(document);
	}
}
