package com.wouterpot.makeyourshoppinglist.server;

import java.util.List;

import javax.jdo.JDOException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
import com.wouterpot.makeyourshoppinglist.client.GreetingService;
import com.wouterpot.makeyourshoppinglist.server.datastore.Product;
import com.wouterpot.makeyourshoppinglist.server.datastore.ShoppingList;
import com.wouterpot.makeyourshoppinglist.shared.ProductDto;
import com.wouterpot.makeyourshoppinglist.shared.ShoppingListDto;

public class ShoppingListService extends RemoteServiceServlet implements
GreetingService {

	public ShoppingListService() {}

	private void initShoppingList() {
		ShoppingList shoppingList = null;
		PMF.open();
		try {
			PMF.begin();
	
			List<ShoppingList> shoppingLists = PMF.retrieveAll(ShoppingList.class);
			if (shoppingLists.size() > 0) {
				shoppingList = shoppingLists.get(0);
				PMF.retrieve(shoppingList);
			}
		}
		catch (JDOException ex) {
			ex.printStackTrace();
		}
		finally {
			PMF.commit();
			PMF.close();
		}
		if (shoppingList != null)
			ShoppingListFactory.get().setShoppingList(shoppingList);
	}
	
	@Override
	public void init() throws javax.servlet.ServletException {
		super.init();
		initShoppingList();
	};

	private static final long serialVersionUID = 1L;

	@Override
	public ShoppingListDto greetServer(String[] sites) throws IllegalArgumentException {
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
		PMF.open();
		PMF.begin();
		
		shoppingList = PMF.getObjectById(ShoppingList.class, shoppingList.getId());
		for (ProductDto clientProduct : clientProducts) {
			List<Product> products = shoppingList.getProduct(clientProduct.getCategoryName(), clientProduct.getIds());
			for (Product product : products) {
				product.setVisible(clientProduct.getVisible());
			}	
		}
		
		PMF.commit();
		PMF.close();
	}

	@Override
	public void createNewShoppingList() {
		ShoppingListFactory.get().createNewShoppingList();
	}
}
