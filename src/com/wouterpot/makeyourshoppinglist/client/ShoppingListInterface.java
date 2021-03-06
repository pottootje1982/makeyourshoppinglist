package com.wouterpot.makeyourshoppinglist.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.wouterpot.makeyourshoppinglist.shared.ProductDto;
import com.wouterpot.makeyourshoppinglist.shared.WelcomeDto;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface ShoppingListInterface extends RemoteService {
	WelcomeDto greetServer(String[] sites) throws IllegalArgumentException;
	void productVisibilityChange(List<ProductDto> clientProducts);
	void createNewShoppingList();
	WelcomeDto addCustomIngredient(String ingredient, String language);
}
