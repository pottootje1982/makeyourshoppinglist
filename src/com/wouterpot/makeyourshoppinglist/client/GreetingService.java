package com.wouterpot.makeyourshoppinglist.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.wouterpot.makeyourshoppinglist.shared.ClientProduct;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	Map<String, ArrayList<ClientProduct>> greetServer(String[] sites) throws IllegalArgumentException;
	void productVisibilityChange(List<ClientProduct> clientProducts);
}
