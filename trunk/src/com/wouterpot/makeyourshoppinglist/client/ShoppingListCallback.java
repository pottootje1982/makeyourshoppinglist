package com.wouterpot.makeyourshoppinglist.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.wouterpot.makeyourshoppinglist.shared.WelcomeDto;

final class ShoppingListCallback implements	AsyncCallback<WelcomeDto> {
	
	private ShoppingListEntryPoint shoppingListEntryPoint;

	public ShoppingListCallback(ShoppingListEntryPoint shoppingListEntryPoint) {
		this.shoppingListEntryPoint = shoppingListEntryPoint;
	}

	public void onFailure(Throwable caught) {
		shoppingListEntryPoint.showErrorMessage("Error while trying to contact server...\n" + caught.getMessage());
	}

	public void onSuccess(WelcomeDto shoppingListDto) {
		shoppingListEntryPoint.fillShoppingList(shoppingListDto);
	}
}