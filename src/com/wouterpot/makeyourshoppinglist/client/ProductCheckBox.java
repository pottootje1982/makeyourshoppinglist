package com.wouterpot.makeyourshoppinglist.client;

import com.google.gwt.user.client.ui.CheckBox;
import com.wouterpot.makeyourshoppinglist.shared.ProductDto;

public class ProductCheckBox extends CheckBox {

	private ProductDto product;

	public ProductCheckBox(String label, ProductDto product) {
		super(label);
		this.product = product;
	}

	public ProductDto getProduct() {
		return product;
	}


}
