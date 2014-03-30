package com.wouterpot.makeyourshoppinglist.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.wouterpot.makeyourshoppinglist.client.GreetingService;
import com.wouterpot.makeyourshoppinglist.server.datastore.Product;
import com.wouterpot.makeyourshoppinglist.server.datastore.ShoppingList;
import com.wouterpot.makeyourshoppinglist.shared.ClientProduct;

public class ShoppingListService extends RemoteServiceServlet implements
GreetingService {

	public ShoppingListService() {
		initShoppingList();
	}

	private void initShoppingList() {
		PMF.open();

		List<ShoppingList> shoppingLists = PMF.retrieveAll(ShoppingList.class);
		ShoppingList shoppingList = null;
		if (shoppingLists.size() > 0) {
			shoppingList = shoppingLists.get(0);
			PMF.retrieve(shoppingList);
		}
		
		PMF.close();
		if (shoppingList != null)
			ShoppingListFactory.get().setShoppingList(shoppingList);
	};

	private static final long serialVersionUID = 1L;

	@Override
	public Map<String, ArrayList<ClientProduct>> greetServer(String[] sites) throws IllegalArgumentException {
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		for (String site : sites) {
			shoppingListFactory.addToShoppingList(site);	
		}
		
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		
		return shoppingList.getShoppingList();
	}

	@Override
	public void productVisibilityChange(List<ClientProduct> clientProducts) {
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		for (ClientProduct clientProduct : clientProducts) {
			Product product = shoppingList.getProduct(clientProduct.getCategoryName(), clientProduct.getId());
			if (product != null)
				product.setVisible(clientProduct.getVisible());
		}
	}
}
