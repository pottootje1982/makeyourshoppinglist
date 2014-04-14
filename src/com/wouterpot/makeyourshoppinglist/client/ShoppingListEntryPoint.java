package com.wouterpot.makeyourshoppinglist.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.wouterpot.makeyourshoppinglist.client.handlers.AddShoppingItemTextBoxKeyHandler;
import com.wouterpot.makeyourshoppinglist.client.handlers.AddSiteTextBoxHandler;
import com.wouterpot.makeyourshoppinglist.client.handlers.CheckboxClickHandler;
import com.wouterpot.makeyourshoppinglist.client.handlers.ClearShoppingListHandler;
import com.wouterpot.makeyourshoppinglist.client.handlers.HideButtonHandler;
import com.wouterpot.makeyourshoppinglist.client.handlers.HyperlinkHandler;
import com.wouterpot.makeyourshoppinglist.shared.ProductDto;
import com.wouterpot.makeyourshoppinglist.shared.WelcomeDto;

// TODO: option to expand added items (to not add)
// TODO: setting page where you can set language
// TODO: create dropdown where you can select different shopping lists
// TODO: create settings page where you can define categories
// TODO: adding custom items shouldn't erase checked checkboxes

public class ShoppingListEntryPoint implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final ShoppingListInterfaceAsync greetingService = GWT.create(ShoppingListInterface.class);
	private VerticalPanel recipePanel;
	private VerticalPanel shoppingListPanel;
	private Map<String, ArrayList<ProductCheckBox>> shoppingList = new HashMap<String, ArrayList<ProductCheckBox>>();
	private TextBox addSiteTextBox;
	private ShoppingListCallback shoppingListCallback;
	private Anchor signInAnchor;

	@Override
	public void onModuleLoad() {
		HorizontalPanel addSitePanel = new HorizontalPanel();
		addSitePanel.add(new Label("Add recipe:"));
		addSiteTextBox = new TextBox();
		addSiteTextBox.setWidth("820");
		addSiteTextBox.addKeyPressHandler(new AddSiteTextBoxHandler(this));
		addSitePanel.add(addSiteTextBox);
		signInAnchor = new Anchor("Sign in");
		addSitePanel.add(signInAnchor);
		RootPanel.get().add(addSitePanel);
		
		Label label = new Label("Recipes:");
		RootPanel.get().add(label);

		recipePanel = new VerticalPanel();
		int parameterIndex = 1;
		String site = null;
		ArrayList<String> sites = new ArrayList<String>();
		while ((site = com.google.gwt.user.client.Window.Location.getParameter("site" + parameterIndex++)) != null) {
			sites.add(site);
		}
		String[] sitesArray = new String[sites.size()];
		RootPanel.get().add(recipePanel);

		shoppingListPanel = new VerticalPanel();
		RootPanel.get().add(shoppingListPanel);
		shoppingListCallback = new ShoppingListCallback(this);
		querySites(sites.toArray(sitesArray));

		RootPanel.get().add(new Label("Add custom shopping item:"));
		TextBox customShoppingItemCheckbox = new TextBox();
		customShoppingItemCheckbox.addKeyPressHandler(new AddShoppingItemTextBoxKeyHandler(this));
		RootPanel.get().add(customShoppingItemCheckbox);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		Button hideButton = new Button("Hide checked items");
		hideButton.addClickHandler(new HideButtonHandler(greetingService, this, false));
		Button unhideButton = new Button("Unhide items");
		unhideButton.addClickHandler(new HideButtonHandler(greetingService, this, true));
		Button clearButton = new Button("Clear shopping list");
		clearButton.addClickHandler(new ClearShoppingListHandler(this));
		buttonPanel.add(hideButton);
		buttonPanel.add(unhideButton);
		buttonPanel.add(clearButton);
		RootPanel.get().add(buttonPanel);
	}

	public void querySites(String[] sites) {
		greetingService.greetServer(sites, shoppingListCallback);
	}
	
	public void clearShoppingList() {
		recipePanel.clear();
		shoppingListPanel.clear();
		shoppingList.clear();
		greetingService.createNewShoppingList(new VoidCallback());
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

	public void addIngredient(String ingredient) {
		greetingService.addCustomIngredient(ingredient, "nl", new AsyncCallback<WelcomeDto>() {
			
			@Override
			public void onSuccess(WelcomeDto result) {
				fillShoppingList(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				showErrorMessage("Error while retrieving new version of shopping list: " + caught.getMessage());
				caught.printStackTrace();
			}
		});		
	}

	public void fillShoppingList(WelcomeDto welcomeDto) {
		Map<String, ArrayList<ProductDto>> productsToCategories = welcomeDto.getShoppingListMap();
		shoppingListPanel.clear();
		recipePanel.clear();
		shoppingList.clear();

		for (Map.Entry<String, ArrayList<ProductDto>> entry : productsToCategories.entrySet()) {
			ArrayList<ProductCheckBox> productCheckBoxes = new ArrayList<>();
			shoppingListPanel.add(new Label(entry.getKey()));
			VerticalPanel verticalPanel = new VerticalPanel();
			shoppingListPanel.add(verticalPanel);
			ArrayList<ProductDto> clientProducts = entry.getValue();
			for (ProductDto product : clientProducts) {
				ProductCheckBox checkBox = new ProductCheckBox(product.toString(), product);
				checkBox.addClickHandler(new CheckboxClickHandler(product));
				String aggregatedProductName = product.getAggregatedProductName();
				if (aggregatedProductName != null) {
					checkBox.setTitle(aggregatedProductName);
					checkBox.getElement().getStyle().setFontWeight(FontWeight.BOLD);
				}
				checkBox.getElement().getStyle().setFontStyle(product.isCustom() == Boolean.TRUE ? FontStyle.ITALIC : FontStyle.NORMAL);
				boolean visible = product.getVisible() != Boolean.FALSE;
				checkBox.setVisible(visible);
				checkBox.setValue(!visible);
				verticalPanel.add(checkBox);
				productCheckBoxes.add(checkBox);
			}
			this.shoppingList.put(entry.getKey(), productCheckBoxes);
		}
		
		List<String> sites = welcomeDto.getSites();
		for (String site : sites) {
			Anchor hyperlink = new Anchor(site);
			hyperlink.addClickHandler(new HyperlinkHandler(site));
			recipePanel.add(hyperlink);
		}
		
		signInAnchor.setHref(welcomeDto.getSignInUrl());
	}
	
	public void showErrorMessage(String message) {
		RootPanel.get().add(new Label(message));
	}

	public Map<String, ArrayList<ProductCheckBox>> getShoppingList() {
		return shoppingList;
	}
}
