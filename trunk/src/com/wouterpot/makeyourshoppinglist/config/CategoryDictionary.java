package com.wouterpot.makeyourshoppinglist.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.appengine.labs.repackaged.com.google.common.base.Strings;
import com.wouterpot.makeyourshoppinglist.helpers.RegEx;
import com.wouterpot.makeyourshoppinglist.helpers.Resource;
import com.wouterpot.makeyourshoppinglist.server.Product;

public class CategoryDictionary {
	private static final String COMMON = "[common]";
	private static final String EXCLUDE = "[exclude]";
	
	ArrayList<String> categories = new ArrayList<String>();
	private String[] categoryFiles;

	Map<String, ArrayList<ProductInfo>> categoriesToProductInfos = new HashMap<String, ArrayList<ProductInfo>>();
	private String language;

	private String getFileBase(String name) {
		int point = name.lastIndexOf(".");
		return point > 0 ? name.substring(0, point) : name;
	}

	public CategoryDictionary(String language, String path)
			throws URISyntaxException, IOException {
		this.language = language;
		categoryFiles = Resource.getResourceListing(getClass(), path + "/" + language + "/");

		for (String categoryFile : categoryFiles) {
			categories.add(getFileBase(categoryFile));
		}

		fillStoresTable(path);
	}

	private void put(Map<String, ArrayList<ProductInfo>> categoriesToProducts,
			String category, ProductInfo product) {
		ArrayList<ProductInfo> products = categoriesToProducts.get(category);
		if (products == null) {
			products = new ArrayList<ProductInfo>();
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
						ProductInfo product = new ProductInfo(productKey, category, isExcluded, section.equals(COMMON));

						put(categoriesToProductInfos, category, product);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Product getProduct(String ingredient) {
		ingredient = RegEx.escapeStrangeChars(ingredient);
		for (String category : categoriesToProductInfos.keySet()) {
			ArrayList<ProductInfo> products = categoriesToProductInfos.get(category);
			for (ProductInfo productInfo : products) {
				if (productInfo.productMatches(ingredient))
					return productInfo.isExcluded() ? null : new Product(ingredient, productInfo);
			}
		}
		return null;
	}


}
