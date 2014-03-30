package com.wouterpot.makeyourshoppinglist.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.wouterpot.makeyourshoppinglist.client.GreetingService;
import com.wouterpot.makeyourshoppinglist.server.datastore.Product;
import com.wouterpot.makeyourshoppinglist.server.datastore.ShoppingList;
import com.wouterpot.makeyourshoppinglist.shared.ProductDto;

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
	public Map<String, ArrayList<ProductDto>> greetServer(String[] sites) throws IllegalArgumentException {
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		for (String site : sites) {
			shoppingListFactory.addToShoppingList(site);	
		}
		
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		
		return shoppingList.getShoppingList();
	}

	@Override
	public void productVisibilityChange(List<ProductDto> clientProducts) {
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		for (ProductDto clientProduct : clientProducts) {
			List<Product> products = shoppingList.getProduct(clientProduct.getCategoryName(), clientProduct.getIds());
			for (Product product : products) {
				product.setVisible(clientProduct.getVisible());
			}	
		}
	}
}
