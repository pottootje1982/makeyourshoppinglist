package com.wouterpot.makeyourshoppinglist.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
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
		shoppingListEntryPoint.fillShoppingList(shoppingListDto);
	}
}