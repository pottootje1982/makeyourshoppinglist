package com.wouterpot.makeyourshoppinglist.server;

import java.util.List;

import javax.jdo.JDOException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.wouterpot.makeyourshoppinglist.client.ShoppingListInterface;
import com.wouterpot.makeyourshoppinglist.server.datastore.Product;
import com.wouterpot.makeyourshoppinglist.server.datastore.ShoppingList;
import com.wouterpot.makeyourshoppinglist.shared.ProductDto;
import com.wouterpot.makeyourshoppinglist.shared.ShoppingListDto;

// TODO: shouldn't fail on url's starting with www.

public class ShoppingListInterfaceImpl extends RemoteServiceServlet implements
ShoppingListInterface {

	public ShoppingListInterfaceImpl() {}

	private void initShoppingList() {
		ShoppingList shoppingList = null;
		PMF.open();
		try {
			PMF.begin();
	
			List<ShoppingList> shoppingLists = PMF.retrieveAll(ShoppingList.class);
			if (shoppingLists.size() > 0) {
				shoppingList = shoppingLists.get(shoppingLists.size() - 1);
				PMF.retrieve(shoppingList);
			}
			
			PMF.commit();
		}
		catch (JDOException ex) {
			ex.printStackTrace();
		}
		finally {
			PMF.rollback();
			PMF.close();
		}
		if (shoppingList != null)
			ShoppingListFactory.get().setShoppingList(shoppingList);
		else
			ShoppingListFactory.get().createNewShoppingList();
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
		
		return ShoppingList.getShoppingList();
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
		
		PMF.makePersistent(shoppingList);
		
		PMF.commit();
		PMF.close();
	}

	@Override
	public void createNewShoppingList() {
		ShoppingListFactory.get().createNewShoppingList();
	}

	@Override
	public ShoppingListDto addCustomIngredient(String ingredient, String language) {
		ShoppingList shoppingList = ShoppingListFactory.get().getShoppingList();		
		shoppingList.addCustomIngredient(ingredient, language);
		return ShoppingList.getShoppingList();
	}
}
