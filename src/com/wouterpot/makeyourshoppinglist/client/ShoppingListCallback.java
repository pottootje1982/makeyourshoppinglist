package com.wouterpot.makeyourshoppinglist.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.wouterpot.makeyourshoppinglist.shared.ProductDto;
import com.wouterpot.makeyourshoppinglist.shared.ShoppingListDto;

final class ShoppingListCallback implements	AsyncCallback<ShoppingListDto> {
	
	private ShoppingListEntryPoint shoppingListEntryPoint;

	public ShoppingListCallback(ShoppingListEntryPoint shoppingListEntryPoint) {
		this.shoppingListEntryPoint = shoppingListEntryPoint;
	}

	public void onFailure(Throwable caught) {
		shoppingListEntryPoint.showErrorMessage("Error while trying to contact server...\n" + caught.getMessage());
	}

	public void onSuccess(ShoppingListDto shoppingListDto) {
		shoppingListEntryPoint.showShoppingList(shoppingListDto);
	}
}