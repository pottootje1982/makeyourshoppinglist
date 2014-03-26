package com.wouterpot.makeyourshoppinglist.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.gargoylesoftware.htmlunit.javascript.host.Console;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.wouterpot.makeyourshoppinglist.client.GreetingService;
import com.wouterpot.makeyourshoppinglist.server.datastore.Category;
import com.wouterpot.makeyourshoppinglist.server.datastore.Product;
import com.wouterpot.makeyourshoppinglist.server.datastore.ShoppingList;

public class ShoppingListService extends RemoteServiceServlet implements
GreetingService {

	public ShoppingListService() {
		initShoppingList();
	}

	private void initShoppingList() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.currentTransaction().begin();
		Query newQuery = pm.newQuery(ShoppingList.class);
		List<ShoppingList> shoppingLists = (List<ShoppingList>)newQuery.execute();
		pm.retrieveAll(shoppingLists);
		ShoppingList shoppingList = null;
		if (shoppingLists.size() > 0) {
			shoppingList = shoppingLists.get(0);
			pm.retrieve(shoppingList);
		}
		pm.currentTransaction().commit();
		pm.close();
		if (shoppingList != null)
			ShoppingListFactory.get().setShoppingList(shoppingList);
	};

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
