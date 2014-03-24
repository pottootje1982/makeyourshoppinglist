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

	}

	private static final long serialVersionUID = 1L;

	@Override
	public Map<String, ArrayList<String>> greetServer(String[] sites) throws IllegalArgumentException {
		ShoppingListFactory shoppingListFactory = new ShoppingListFactory();
		for (String site : sites) {
			shoppingListFactory.addToShoppingList(site);	
		}
		
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		
		try {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query newQuery = pm.newQuery(ShoppingList.class);
			List<ShoppingList> streamingQueryResult = (List<ShoppingList>)newQuery.execute();
			ShoppingList shoppingList2 = null;
			if (streamingQueryResult.size() > 0)
				shoppingList2 = streamingQueryResult.get(0);

			shoppingList = pm.makePersistent(shoppingList);
			pm.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return shoppingList.getShoppingList();
	}
}
