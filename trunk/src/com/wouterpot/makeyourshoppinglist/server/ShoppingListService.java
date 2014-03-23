package com.wouterpot.makeyourshoppinglist.server;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.wouterpot.makeyourshoppinglist.client.GreetingService;

public class ShoppingListService extends RemoteServiceServlet implements
GreetingService {

	private static final long serialVersionUID = 1L;

	@Override
	public Map<String, ArrayList<String>> greetServer(String[] sites) throws IllegalArgumentException {
		ShoppingListFactory shoppingListFactory = new ShoppingListFactory();
		for (String site : sites) {
			shoppingListFactory.addToShoppingList(site);	
		}
		
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		return shoppingList.getShoppingList();
	}
}
