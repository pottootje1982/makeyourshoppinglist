package com.wouterpot.makeyourshoppinglist.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

final class ClearShoppingListHandler implements ClickHandler {
	private ShoppingListEntryPoint shoppingListEntryPoint;

	public ClearShoppingListHandler(ShoppingListEntryPoint shoppingListEntryPoint) {
				this.shoppingListEntryPoint = shoppingListEntryPoint;
	}

	@Override
	public void onClick(ClickEvent event) {
		shoppingListEntryPoint.clearShoppingList();
	}
}