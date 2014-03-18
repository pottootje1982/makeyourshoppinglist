package com.wouterpot.makeyourshoppinglist;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class IngredientsScraper {
	List<SiteInfo> siteInfos = new ArrayList<>();
	
	public IngredientsScraper()
	{
		getIngredientsClasses("res/ingredientsClasses.xml");
	}
	
	private void getIngredientsClasses(String url)
	{
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse("res/ingredientsClasses.xml");
			Node sites = doc.getElementsByTagName("sites").item(0);
			NodeList childNodes = sites.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node child = childNodes.item(i);
				NamedNodeMap attributes = child.getAttributes();
				if (attributes != null)
					siteInfos.add(new SiteInfo(attributes));
			}
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public IngredientsList getIngredients(String url) throws IOException
	{
		Document document = Jsoup.connect(url).get();

	    for (SiteInfo siteInfo : siteInfos) { 
	    	if (!siteInfo.matchesSite(url)) continue; 
	    
	        Elements ingredients = new Elements();
	        if (siteInfo.getClassName() != null) { 
	        	ingredients = document.getElementsByClass(siteInfo.getClassName());
	        } 
	        else if (siteInfo.getId() != null) 
	        	ingredients.add(document.getElementById(siteInfo.getId())); 
	        String tagName = siteInfo.getTagName();
			if (tagName != null) { 
	        	ingredients = ingredients.size() != 0 ? ingredients.get(0).getElementsByTag(tagName) : document.getElementsByTag(tagName); 
	        } 
	        if (ingredients.size() > 0) { 
	          return siteInfo.createIngredientsList(ingredients); 
	        } 
	    }
	    return null;
	}
}
