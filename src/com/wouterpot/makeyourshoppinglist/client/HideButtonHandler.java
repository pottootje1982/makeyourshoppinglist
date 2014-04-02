package com.wouterpot.makeyourshoppinglist.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.wouterpot.makeyourshoppinglist.shared.ProductDto;

final class HideButtonHandler implements ClickHandler {

	private ShoppingListEntryPoint shoppingListEntryPoint;
	private boolean unhide;
	private ShoppingListInterfaceAsync greetingService;

	public HideButtonHandler(ShoppingListInterfaceAsync greetingService, ShoppingListEntryPoint shoppingListEntryPoint, boolean unhide) {
		this.greetingService = greetingService;
		this.shoppingListEntryPoint = shoppingListEntryPoint;
		this.unhide = unhide;
	}

	@Override
	public void onClick(ClickEvent event) {
		List<ProductDto> clientProducts = new ArrayList<>();
		for (Entry<String, ArrayList<ProductCheckBox>> category : shoppingListEntryPoint.getShoppingList().entrySet()) {
			for (ProductCheckBox productCheckBox : category.getValue()) {
				if (productCheckBox.getValue()) {
					productCheckBox.setVisible(unhide);
					productCheckBox.getProduct().setVisible(unhide);
					clientProducts.add(productCheckBox.getProduct());
				}
			}
		}
		greetingService.productVisibilityChange(clientProducts, new VoidCallback());
	}
}