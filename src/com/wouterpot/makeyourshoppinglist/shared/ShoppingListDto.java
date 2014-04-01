package com.wouterpot.makeyourshoppinglist.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ShoppingListDto implements Serializable {

	private Map<String, ArrayList<ProductDto>> shoppingListMap;
	private List<String> sites;
	
	public ShoppingListDto() {

	}

	public ShoppingListDto(Map<String, ArrayList<ProductDto>> shoppingListMap, List<String> sites) {
				this.shoppingListMap = shoppingListMap;
				this.sites = sites;
	}

	public Map<String, ArrayList<ProductDto>> getShoppingListMap() {
		return shoppingListMap;
	}

	public List<String> getSites() {
		return sites;
	}
}
