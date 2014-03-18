package com.wouterpot.makeyourshoppinglist;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.appengine.labs.repackaged.com.google.common.base.Strings;

public class CategoryDictionary {
	String[] languages  = {"en", "nl"};
	ArrayList<String> categories = new ArrayList<>(); 
	private File[] categoryFiles;

	Map<String, ArrayList<Product>> categoriesToProducts = new HashMap<String, ArrayList<Product>>();
	Map<String, ArrayList<Product>> excludedProducts = new HashMap<String, ArrayList<Product>>();
	private String language;
	
	private String getFileBase(File file)
	{
		String name = file.getName();
		int point = name.lastIndexOf(".");
		return point > 0 ? name.substring(0, point) : name;
	}
	
	public CategoryDictionary(String language, String path)
	{
		this.language = language;
		URL url = getClass().getResource(path + "/" + language + "/");
		File dir;
		try {
			dir = new File(path + "/" + language + "/");
			dir = new File(url.toURI());
		    categoryFiles = dir.listFiles();
		    for (File categoryFile : categoryFiles)
		    {
		    	categories.add(getFileBase(categoryFile));
		    }
		    	
			fillStoresTable();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	private void put(Map<String, ArrayList<Product>> categoriesToProducts, String category, Product product)
	{
		ArrayList<Product> products = categoriesToProducts.get(category);
		if (products == null)
		{
			products = new ArrayList<>();
			categoriesToProducts.put(category, products);
		}
		products.add(product);
	}
	
	private void fillStoresTable() { 
		for (String category : categories)
		{
			InputStream inputstream = getClass().getResourceAsStream("/data/" + language + "/" + category + ".txt");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream));
			String line = null;
			String section = "";
			try {
				while ((line = bufferedReader.readLine()) != null)
				{
					String productKey = line.trim();
					Pattern pat = Pattern.compile("^([.*])$");
					Matcher matcher = pat.matcher(productKey);
					boolean matchesSection = productKey.matches("^([.*])$");
					if (matchesSection) section = matcher.group().toLowerCase();
					else if (!Strings.isNullOrEmpty(productKey))
					{
						boolean isExcluded = section == "[exclude]";
						Product product = new Product(productKey.toLowerCase(), isExcluded, section == "[common]");
						
						if (isExcluded)
							put(excludedProducts, category, product);
						else
							put(categoriesToProducts, category, product);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getCategory(String productKey) {
		for (String category : categoriesToProducts.keySet())
		{
			ArrayList<Product> products = categoriesToProducts.get(category);
			for (Product product : products)
			{
				if (productKey.matches(product.getProductKey()))
					return category;
			}
		}
		return null;
	} 
}
