package com.wouterpot.makeyourshoppinglist.config;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.datanucleus.util.StringUtils;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wouterpot.makeyourshoppinglist.helpers.Resource;
import com.wouterpot.makeyourshoppinglist.server.IngredientsList;

public class SiteInfo
{
	private String language;
	private String tagName;
	private String className;
	private String id;
	private String childTagName;
	private String childClassName;
	@PrimaryKey
	@Persistent
	private String url;

	private static String getAttribute(Attributes attributes, String name) {
		String item = attributes.get(name);
		return !StringUtils.isEmpty(item) ? item : null;
	}
	
	public SiteInfo(Attributes attributes) {
		this(getAttribute(attributes, "language"),
			getAttribute(attributes, "tagName"),
			getAttribute(attributes, "class"),
			getAttribute(attributes, "id"),
			getAttribute(attributes, "childTagName"),
			getAttribute(attributes, "childClassName"),
			getAttribute(attributes, "url"));
	}

	public IngredientsList createIngredientsList(Elements ingredients) {
		List<String> resIngredients = new ArrayList<String>();
		if (getChildTagName() != null) {
			for (int i = 0; i < ingredients.size(); i++) {
				Elements innerIngredients = ingredients.get(i).getElementsByTag(getChildTagName());
				for (Element element : innerIngredients) {
					if (	childClassName == null || element.attr("class").equals(childClassName) && 
							childTagName == null || element.attr("tag").equals(childTagName)) {
						addIngredient(resIngredients, element);
					}
				}
			}
		}
		else {
			// If site doesn't have a child tag name, it's a blob so we should replace
			// <br> with newline
			String html = ingredients.html();
			resIngredients = Resource.splitBlobByBreak(html);
		}
		return new IngredientsList(this, resIngredients);
	}

	private void addIngredient(List<String> resIngredients, Element element) {
		String ingredient = element.text().trim();
		if (!StringUtils.isEmpty(ingredient))
			resIngredients.add(ingredient);
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

	@Override
	public String toString() {
		return "SiteInfo [language=" + language + ", tagName=" + tagName
				+ ", className=" + className + ", id=" + id + ", childTagName="
				+ childTagName + ", childClassName=" + childClassName
				+ ", url=" + url + "]";
	}
}