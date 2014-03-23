package com.wouterpot.makeyourshoppinglist.client;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String[] sites, AsyncCallback<Map<String, ArrayList<String>>> callback)
			throws IllegalArgumentException;
}
