package com.wouterpot.makeyourshoppinglist.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.wouterpot.makeyourshoppinglist.shared.ProductDto;
import com.wouterpot.makeyourshoppinglist.shared.ShoppingListDto;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String[] sites, AsyncCallback<ShoppingListDto> callback)
			throws IllegalArgumentException;

	void productVisibilityChange(List<ProductDto> clientProducts, AsyncCallback<Void> callback);
}
