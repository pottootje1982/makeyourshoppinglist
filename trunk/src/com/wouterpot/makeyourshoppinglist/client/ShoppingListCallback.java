package com.wouterpot.makeyourshoppinglist.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.wouterpot.makeyourshoppinglist.shared.ProductDto;

final class ShoppingListCallback implements	AsyncCallback<Map<String, ArrayList<ProductDto>>> {
	
	private Map<String, ArrayList<ProductCheckBox>> shoppingList = new HashMap<String, ArrayList<ProductCheckBox>>();

	public void onFailure(Throwable caught) {
		RootPanel.get().add(new Label("Error while trying to contact server...\n" + caught.getMessage()));
	}

	public void onSuccess(Map<String, ArrayList<ProductDto>> shoppingList) {
		VerticalPanel shoppingListWidget = (VerticalPanel) ShoppingListEntryPoint.findWidget("shoppingList");

		for (Map.Entry<String, ArrayList<ProductDto>> entry : shoppingList.entrySet()) {
			ArrayList<ProductCheckBox> productCheckBoxes = new ArrayList<>();
			shoppingListWidget.add(new Label(entry.getKey()));
			VerticalPanel verticalPanel = new VerticalPanel();
			shoppingListWidget.add(verticalPanel);
			ArrayList<ProductDto> clientProducts = entry.getValue();
			for (ProductDto product : clientProducts) {
				ProductCheckBox checkBox = new ProductCheckBox(product.toString(), product);
				checkBox.addClickHandler(new CheckboxClickHandler(product));
				checkBox.setTitle(product.getAggregatedProductName());
				verticalPanel.add(checkBox);
				productCheckBoxes.add(checkBox);
				checkBox.setVisible(product.getVisible() != Boolean.FALSE);
			}
			this.shoppingList.put(entry.getKey(), productCheckBoxes);
		}
	}

	public Map<String, ArrayList<ProductCheckBox>> getShoppingList() {
		return shoppingList;
	}
}