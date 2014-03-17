package com.wouterpot.makeyourshoppinglist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class IngredientsScraper {
	private class SiteInfo
	{
		private Node language;
		private Node tagName;
		private Node className;
		private Node id;
		private Node childTagName;
		private Node childClassName;
		private Node url;

		public SiteInfo(NamedNodeMap attributes) {
			setLanguage(attributes.getNamedItem("language"));
			tagName = attributes.getNamedItem("tagName");
			className = attributes.getNamedItem("class");
			id = attributes.getNamedItem("id");
			childTagName = attributes.getNamedItem("childTagName");
			childClassName = attributes.getNamedItem("childClassName");
			url = attributes.getNamedItem("url");
		}

		public Node getLanguage() {
			return language;
		}

		public void setLanguage(Node language) {
			this.language = language;
		}
	}
	
	ArrayList<SiteInfo> siteInfos = new ArrayList<SiteInfo>();
	
	public IngredientsScraper(ServletContext servletContext)
	{
		getIngredientsClasses(servletContext);
	}
	
	private void getIngredientsClasses(ServletContext servletContext)
	{
		EntityManagerFactory createEntityManagerFactory = Persistence.createEntityManagerFactory("transactions-optional");
	
		URL ingredientsClassesUrl = null;
		try {
			ingredientsClassesUrl = servletContext.getResource("/WEB-INF/data/ingredientsClasses.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(ingredientsClassesUrl.openStream());
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
		System.out.println(siteInfos.get(0).getLanguage());
		siteInfos.size();
	}
}
