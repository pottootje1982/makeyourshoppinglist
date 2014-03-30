package com.wouterpot.makeyourshoppinglist.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
	
public class ShoppingListEntryPoint implements EntryPoint {

	private final class HyperlinkHandler implements ClickHandler {
		private String site;

		public HyperlinkHandler(String site) {
			this.site = site;
		}

		@Override
		public void onClick(ClickEvent event) {
			Window.open(site, "_blank", "");
		}
	}

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	@Override
	public void onModuleLoad() {
		Label label = new Label("Recipes:");

		RootPanel.get().add(label);

		int parameterIndex = 1;
		String site = null;
		ArrayList<String> sites = new ArrayList<String>();
		while ((site = com.google.gwt.user.client.Window.Location
				.getParameter("site" + parameterIndex++)) != null) {
			sites.add(site);
			Anchor hyperlink = new Anchor(site);
			hyperlink.addClickHandler(new HyperlinkHandler(site));
			RootPanel.get().add(hyperlink);
		}
		String[] sitesArray = new String[sites.size()];

		VerticalPanel shoppingListPanel = new VerticalPanel();
		shoppingListPanel.getElement().setId("shoppingList");
		RootPanel.get().add(shoppingListPanel);
		ShoppingListCallback shoppingListCallback = new ShoppingListCallback();
		greetingService.greetServer(sites.toArray(sitesArray), shoppingListCallback);

		HorizontalPanel buttonPanel = new HorizontalPanel();
		Button hideButton = new Button("Hide checked items");
		hideButton.addClickHandler(new HideButtonHandler(greetingService, shoppingListCallback, false));
		Button unhideButton = new Button("Unhide items");
		unhideButton.addClickHandler(new HideButtonHandler(greetingService, shoppingListCallback, true));
		buttonPanel.add(hideButton);
		buttonPanel.add(unhideButton);
		RootPanel.get().add(buttonPanel);
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