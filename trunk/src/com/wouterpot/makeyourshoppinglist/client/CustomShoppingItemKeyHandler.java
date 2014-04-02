package com.wouterpot.makeyourshoppinglist.client;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.TextBox;

final class CustomShoppingItemKeyHandler implements KeyPressHandler {
	private ShoppingListEntryPoint shoppingListEntryPoint;

	public CustomShoppingItemKeyHandler(ShoppingListEntryPoint shoppingListEntryPoint) {
		this.shoppingListEntryPoint = shoppingListEntryPoint;
	}

	@Override
	public void onKeyPress(KeyPressEvent event) {
		if (event.getCharCode() == KeyCodes.KEY_ENTER){
			TextBox source = (TextBox) event.getSource();
			shoppingListEntryPoint.addIngredient(source.getText());
			source.setText("");
		}
	}
}