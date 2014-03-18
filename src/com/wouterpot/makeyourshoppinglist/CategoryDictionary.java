package com.wouterpot.makeyourshoppinglist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.appengine.labs.repackaged.com.google.common.base.Strings;
import com.wouterpot.makeyourshoppinglist.test.Resources;

public class CategoryDictionary {
	private static final String COMMON = "[common]";
	private static final String EXCLUDE = "[exclude]";
	
	ArrayList<String> categories = new ArrayList<>();
	private String[] categoryFiles;

	Map<String, ArrayList<Product>> categoriesToProducts = new HashMap<String, ArrayList<Product>>();
	private String language;

	private String getFileBase(String name) {
		int point = name.lastIndexOf(".");
		return point > 0 ? name.substring(0, point) : name;
	}

	public CategoryDictionary(String language, String path)
			throws URISyntaxException, IOException {
		this.language = language;
		categoryFiles = Resources.getResourceListing(getClass(), path + "/" + language + "/");

		for (String categoryFile : categoryFiles) {
			categories.add(getFileBase(categoryFile));
		}

		fillStoresTable(path);
	}

	private void put(Map<String, ArrayList<Product>> categoriesToProducts,
			String category, Product product) {
		ArrayList<Product> products = categoriesToProducts.get(category);
		if (products == null) {
			products = new ArrayList<>();
			categoriesToProducts.put(category, products);
		}
		if (product.isExcluded())
			products.add(0, product);
		else
			products.add(product);
	}

	private void fillStoresTable(String path) {
		for (String category : categories) {
			String fullPath = String.format("/%s/%s/%s.txt", path, language, category);
			InputStream inputstream = getClass().getResourceAsStream(fullPath);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(inputstream));
			String line = null;
			String section = "";
			try {
				while ((line = bufferedReader.readLine()) != null) {
					String productKey = line.trim().toLowerCase();

					String match = RegEx.match(productKey, "^(\\[.*\\])$");
					if (match != null)
						section = match.toLowerCase();
					else if (productKey.equals(EXCLUDE)) {}
					else if (productKey.startsWith("#")) {}
					else if (!Strings.isNullOrEmpty(productKey)) {
						boolean isExcluded = section.equals(EXCLUDE);
						Product product = new Product(productKey, isExcluded, section.equals(COMMON));


						put(categoriesToProducts, category, product);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getCategory(String productName) {
		productName = RegEx.escapeStrangeChars(productName);
		for (String category : categoriesToProducts.keySet()) {
			ArrayList<Product> products = categoriesToProducts.get(category);
			for (Product product : products) {
				String matchString =  "\\S*" + product.getProductKey() + "\\S*";
				String match = RegEx.find(productName, matchString);
				if (match != null 
						&& (double)product.getProductKey().length() / match.length() >= 0.5
						&& productName.startsWith(match))
					return product.isExcluded() ? null : category;
			}
		}
		return null;
	}
}
