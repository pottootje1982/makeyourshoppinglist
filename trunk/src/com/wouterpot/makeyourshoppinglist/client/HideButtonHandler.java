package com.wouterpot.makeyourshoppinglist.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.wouterpot.makeyourshoppinglist.shared.ProductDto;

final class HideButtonHandler implements ClickHandler {

	private final class VoidCallback implements AsyncCallback<Void> {
		@Override
		public void onSuccess(Void result) {
		}

		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
		}
	}

	private ShoppingListCallback shoppingList;
	private boolean unhide;
	private GreetingServiceAsync greetingService;

	public HideButtonHandler(GreetingServiceAsync greetingService, ShoppingListCallback shoppingListCallback, boolean unhide) {
		this.greetingService = greetingService;
		this.shoppingList = shoppingListCallback;
		this.unhide = unhide;
	}

	@Override
	public void onClick(ClickEvent event) {
		List<ProductDto> clientProducts = new ArrayList<>();
		for (Entry<String, ArrayList<ProductCheckBox>> category : shoppingList.getShoppingList().entrySet()) {
			for (ProductCheckBox productCheckBox : category.getValue()) {
				if (productCheckBox.getValue()) {
					productCheckBox.setVisible(unhide);
					//productCheckBox.getProduct().setVisible(unhide);
					clientProducts.add(productCheckBox.getProduct());
				}
			}
		}
		greetingService.productVisibilityChange(clientProducts, new VoidCallback());
	}
}