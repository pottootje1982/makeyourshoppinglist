package com.wouterpot.makeyourshoppinglist.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

// TODO: option to expand added items (to not add)

public class ShoppingListEntryPoint implements EntryPoint {

	private final class ClearShoppingListHandler implements ClickHandler {
		private ShoppingListEntryPoint shoppingListEntryPoint;

		public ClearShoppingListHandler(ShoppingListEntryPoint shoppingListEntryPoint) {
					this.shoppingListEntryPoint = shoppingListEntryPoint;
		}

		@Override
		public void onClick(ClickEvent event) {
			shoppingListEntryPoint.clearShoppingList();
		}
	}

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	private VerticalPanel recipePanel;
	private VerticalPanel shoppingListPanel;

	@Override
	public void onModuleLoad() {
		recipePanel = new VerticalPanel();
		
		Label label = new Label("Recipes:");
		recipePanel.add(label);

		int parameterIndex = 1;
		String site = null;
		ArrayList<String> sites = new ArrayList<String>();
		while ((site = com.google.gwt.user.client.Window.Location.getParameter("site" + parameterIndex++)) != null) {
			sites.add(site);
		}
		String[] sitesArray = new String[sites.size()];
		RootPanel.get().add(recipePanel);

		shoppingListPanel = new VerticalPanel();
		shoppingListPanel.getElement().setId("shoppingList");
		RootPanel.get().add(shoppingListPanel);
		ShoppingListCallback shoppingListCallback = new ShoppingListCallback(this);
		greetingService.greetServer(sites.toArray(sitesArray), shoppingListCallback);

		HorizontalPanel buttonPanel = new HorizontalPanel();
		Button hideButton = new Button("Hide checked items");
		hideButton.addClickHandler(new HideButtonHandler(greetingService, shoppingListCallback, false));
		Button unhideButton = new Button("Unhide items");
		unhideButton.addClickHandler(new HideButtonHandler(greetingService, shoppingListCallback, true));
		Button clearButton = new Button("Clear shopping list");
		clearButton.addClickHandler(new ClearShoppingListHandler(this));
		buttonPanel.add(hideButton);
		buttonPanel.add(unhideButton);
		buttonPanel.add(clearButton);
		RootPanel.get().add(buttonPanel);
	}
	
	void clearShoppingList() {
		recipePanel.clear();
		shoppingListPanel.clear();
		greetingService.createNewShoppingList(new VoidCallback());
	}

	void addSiteToPanel(String site) {
		Anchor hyperlink = new Anchor(site);
		hyperlink.addClickHandler(new HyperlinkHandler(site));
		recipePanel.add(hyperlink);
	}

	@SuppressWarnings("unchecked") <T> ArrayList<T> getWidgetsOfType(ComplexPanel panel, Class<T> type) {
		ArrayList<T> result = new ArrayList<>();
		for (int i = 0; i < panel.getWidgetCount(); i++) {
			Widget widget = panel.getWidget(i);
			if (type.equals(widget.getClass()))
				result.add((T) widget);
		}
		return result;
	}

	static Widget findWidget(String id) {
		Widget foundWidget = null;
		for (int i = 0; i < RootPanel.get().getWidgetCount(); i++) {
			Widget widget = RootPanel.get().getWidget(i);
			if (widget.getElement().getId().equals(id))
				foundWidget = widget;
		}
		return foundWidget;
	}
}