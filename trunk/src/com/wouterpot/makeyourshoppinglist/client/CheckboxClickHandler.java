package com.wouterpot.makeyourshoppinglist.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.wouterpot.makeyourshoppinglist.shared.ProductDto;

public class CheckboxClickHandler implements ClickHandler {

	private ProductDto product;

	public CheckboxClickHandler(ProductDto product) {
		this.product = product;
	}

	@Override
	public void onClick(ClickEvent event) {
		product.setVisible(!((CheckBox)event.getSource()).getValue());
	}

}
