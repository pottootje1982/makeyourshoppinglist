package com.wouterpot.makeyourshoppinglist.client;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.TextBox;

final class AddSiteTextBoxHandler implements KeyPressHandler {
	private ShoppingListEntryPoint shoppingListEntryPoint;

	public AddSiteTextBoxHandler(ShoppingListEntryPoint shoppingListEntryPoint) {
		this.shoppingListEntryPoint = shoppingListEntryPoint;
	}

	@Override
	public void onKeyPress(KeyPressEvent event) {
		if (event.getCharCode() == KeyCodes.KEY_ENTER){
			TextBox source = (TextBox) event.getSource();
			shoppingListEntryPoint.querySites(new String[]{source.getText()});
			source.setText("");
		}
	}
}