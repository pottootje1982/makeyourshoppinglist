package com.wouterpot.makeyourshoppinglist.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.wouterpot.makeyourshoppinglist.shared.ProductDto;
import com.wouterpot.makeyourshoppinglist.shared.WelcomeDto;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface ShoppingListInterfaceAsync {
	void greetServer(String[] sites, AsyncCallback<WelcomeDto> callback)
			throws IllegalArgumentException;

	void productVisibilityChange(List<ProductDto> clientProducts, AsyncCallback<Void> callback);

	void createNewShoppingList(AsyncCallback<Void> callback);

	void addCustomIngredient(String ingredient, String language, AsyncCallback<WelcomeDto> callback);
}
