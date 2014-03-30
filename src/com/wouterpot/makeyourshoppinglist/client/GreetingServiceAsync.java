package com.wouterpot.makeyourshoppinglist.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.wouterpot.makeyourshoppinglist.shared.ClientProduct;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String[] sites, AsyncCallback<Map<String, ArrayList<ClientProduct>>> callback)
			throws IllegalArgumentException;

	void productVisibilityChange(List<ClientProduct> clientProducts, AsyncCallback<Void> callback);
}
