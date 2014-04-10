package com.wouterpot.makeyourshoppinglist.client.handlers;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.wouterpot.makeyourshoppinglist.client.ShoppingListEntryPoint;

public final class ClearShoppingListHandler implements ClickHandler {
	private ShoppingListEntryPoint shoppingListEntryPoint;

	public ClearShoppingListHandler(ShoppingListEntryPoint shoppingListEntryPoint) {
				this.shoppingListEntryPoint = shoppingListEntryPoint;
	}

	@Override
	public void onClick(ClickEvent event) {
		shoppingListEntryPoint.clearShoppingList();
	}
}