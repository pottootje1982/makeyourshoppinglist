package com.wouterpot.makeyourshoppinglist.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.wouterpot.makeyourshoppinglist.shared.ProductDto;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String[] sites, AsyncCallback<Map<String, ArrayList<ProductDto>>> callback)
			throws IllegalArgumentException;

	void productVisibilityChange(List<ProductDto> clientProducts, AsyncCallback<Void> callback);
}
