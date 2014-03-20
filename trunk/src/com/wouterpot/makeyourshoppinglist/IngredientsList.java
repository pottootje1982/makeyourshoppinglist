package com.wouterpot.makeyourshoppinglist;

import java.util.ArrayList;

import com.wouterpot.makeyourshoppinglist.config.SiteInfo;

public class IngredientsList {

	private SiteInfo siteInfo;
	private ArrayList<String> ingredients;

	public IngredientsList(SiteInfo siteInfo, ArrayList<String> ingredients) {
		this.siteInfo = siteInfo;
		this.ingredients = ingredients;
	}

	public ArrayList<String> getIngredients() {
		return ingredients;
	}

	public SiteInfo getSiteInfo() {
		return siteInfo;
	}

}
