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
	
	private Map<String, ArrayList<ProductCheckBox>> shoppingList = new HashMap<String, ArrayList<ProductCheckBox>>();
	private ShoppingListEntryPoint shoppingListEntryPoint;

	public ShoppingListCallback(ShoppingListEntryPoint shoppingListEntryPoint) {
		this.shoppingListEntryPoint = shoppingListEntryPoint;
	}

	public void onFailure(Throwable caught) {
		RootPanel.get().add(new Label("Error while trying to contact server...\n" + caught.getMessage()));
	}

	public void onSuccess(ShoppingListDto shoppingListDto) {
		Map<String, ArrayList<ProductDto>> shoppingList = shoppingListDto.getShoppingListMap();
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
				String aggregatedProductName = product.getAggregatedProductName();
				if (aggregatedProductName != null) {
					checkBox.setTitle(aggregatedProductName);
					checkBox.getElement().getStyle().setFontWeight(FontWeight.BOLD);
				}
				boolean visible = product.getVisible() != Boolean.FALSE;
				checkBox.setVisible(visible);
				checkBox.setValue(!visible);
				verticalPanel.add(checkBox);
				productCheckBoxes.add(checkBox);
			}
			this.shoppingList.put(entry.getKey(), productCheckBoxes);
		}
		
		List<String> sites = shoppingListDto.getSites();
		for (String site : sites) {
			shoppingListEntryPoint.addSiteToPanel(site);
		}
	}

	public Map<String, ArrayList<ProductCheckBox>> getShoppingList() {
		return shoppingList;
	}
}