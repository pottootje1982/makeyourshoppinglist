package com.wouterpot.makeyourshoppinglist.config;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import com.wouterpot.makeyourshoppinglist.helpers.Resources;


public class LanguageDictionary {

	private String[] languageDirs;
	Map<String, CategoryDictionary> categoryDictionaries = new HashMap<String, CategoryDictionary>();

	public LanguageDictionary(String path) {
		try {
			languageDirs = Resources.getResourceListing(getClass(), path);
			for (String dir : languageDirs) {
				categoryDictionaries.put(dir, new CategoryDictionary(dir, path));
			}
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public CategoryDictionary getCategoryDictionary(String language) {
		return categoryDictionaries.get(language);
	}
}
