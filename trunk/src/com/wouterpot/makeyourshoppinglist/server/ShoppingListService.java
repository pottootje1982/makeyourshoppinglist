package com.wouterpot.makeyourshoppinglist.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.wouterpot.makeyourshoppinglist.client.GreetingService;
import com.wouterpot.makeyourshoppinglist.server.datastore.ShoppingList;

public class ShoppingListService extends RemoteServiceServlet implements
GreetingService {

	public ShoppingListService() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query newQuery = pm.newQuery(ShoppingList.class);
		List<ShoppingList> shoppingLists = (List<ShoppingList>)newQuery.execute();
		ShoppingList shoppingList = null;
		if (shoppingLists.size() > 0) {
			shoppingList = shoppingLists.get(0);
			pm.retrieve(shoppingList);
			ShoppingListFactory.get().setShoppingList(shoppingList);
		}
		pm.close();
	}

	private static final long serialVersionUID = 1L;

	@Override
	public Map<String, ArrayList<String>> greetServer(String[] sites) throws IllegalArgumentException {
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		for (String site : sites) {
			shoppingListFactory.addToShoppingList(site);	
		}
		
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		
		return shoppingList.getShoppingList();
	}
}
