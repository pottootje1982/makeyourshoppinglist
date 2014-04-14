package com.wouterpot.makeyourshoppinglist.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class WelcomeDto implements Serializable {

	private Map<String, ArrayList<ProductDto>> shoppingListMap;
	private ArrayList<String> sites;
	private String signInUrl;

	public WelcomeDto() {

	}

	public WelcomeDto(Map<String, ArrayList<ProductDto>> shoppingListMap,
			ArrayList<String> sites) {
		this.shoppingListMap = shoppingListMap;
		this.sites = sites;
	}

	public Map<String, ArrayList<ProductDto>> getShoppingListMap() {
		return shoppingListMap;
	}

	public ArrayList<String> getSites() {
		return sites;
	}

	public String getSignInUrl() {
		return signInUrl;
	}

	public void setSignInUrl(String signInUrl) {
		this.signInUrl = signInUrl;
	}
}
