package com.wouterpot.makeyourshoppinglist.client;

import com.google.gwt.user.client.ui.CheckBox;
import com.wouterpot.makeyourshoppinglist.shared.ClientProduct;

public class ProductCheckBox extends CheckBox {

	private ClientProduct product;

	public ProductCheckBox(String label, ClientProduct product) {
		super(label);
		this.product = product;
	}

	public ClientProduct getProduct() {
		return product;
	}


}
