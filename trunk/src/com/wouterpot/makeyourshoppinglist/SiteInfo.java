package com.wouterpot.makeyourshoppinglist;

import java.net.URL;
import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.w3c.dom.NamedNodeMap;


public class SiteInfo
{
	private String language;
	private String tagName;
	private String className;
	private String id;
	private String childTagName;
	private String childClassName;
	private String url;

	private static String getAttribute(NamedNodeMap attributes, String name) {
		org.w3c.dom.Node item = attributes.getNamedItem(name);
		return item != null ? item.getNodeValue() : null;
	}
	
	public SiteInfo(NamedNodeMap attributes) {
		this(getAttribute(attributes, "language"),
			getAttribute(attributes, "tagName"),
			getAttribute(attributes, "class"),
			getAttribute(attributes, "id"),
			getAttribute(attributes, "childTagName"),
			getAttribute(attributes, "childClassName"),
			getAttribute(attributes, "url"));
	}

	public IngredientsList createIngredientsList(Elements ingredients) {
		ArrayList<String> resIngredients = new ArrayList<>();
		if (getChildTagName() != null) {
			for (int i = 0; i < ingredients.size(); i++) {
				Elements innerIngredients = ingredients.get(i)
						.getElementsByTag(getChildTagName());
				for (int j = 0; j < innerIngredients.size(); j++) {
					Element innerIngredient = innerIngredients.get(j);
					if (getChildClassName() == null || innerIngredient.attr("class") == getChildClassName())
						resIngredients.add(innerIngredient.text());
				}
			}
		} else {
			for (int i = 0; i < ingredients.size(); i++) {
				Element innerIngredients = ingredients.get(i);
				for (int j = 0; j < innerIngredients.childNodeSize(); j++) {
					Node ingredientNode = innerIngredients.childNode(j);
					if (ingredientNode instanceof TextNode)
						resIngredients.add(ingredientNode.toString());
				}
			}
		}
		return new IngredientsList(this, resIngredients);
	}

	public SiteInfo(String language, String tagName, String className,
			String id, String childTagName, String childClassName,
			String url) {
		this.language = language;
		this.tagName = tagName;
		this.className = className;
		this.id = id;
		this.childTagName = childTagName;
		this.childClassName = childClassName;
		this.url = url;
	}

	public SiteInfo() {
	}

	public String getLanguage() {
		return language;
	}
	
	public boolean matchesSite(String fullUrl) {
		return url != null && fullUrl.indexOf(url) != -1;
	}

	public String getClassName() {
		return className;
	}

	public String getId() {
		return id;
	}


	public String getTagName() {
		return tagName;
	}

	public String getChildTagName() {
		return childTagName;
	}

	public String getChildClassName() {
		return childClassName;
	}
}