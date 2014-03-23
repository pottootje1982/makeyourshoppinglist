package com.wouterpot.makeyourshoppinglist.server;

import java.util.ArrayList;
import java.util.List;

import com.wouterpot.makeyourshoppinglist.config.SiteInfo;

public class IngredientsList {

	private SiteInfo siteInfo;
	private List<String> ingredients;

	public IngredientsList(SiteInfo siteInfo, List<String> resIngredients) {
		this.siteInfo = siteInfo;
		this.ingredients = resIngredients;
	}

	public List<String> getIngredients() {
		return ingredients;
	}

	public SiteInfo getSiteInfo() {
		return siteInfo;
	}

}
