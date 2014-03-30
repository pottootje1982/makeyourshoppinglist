package com.wouterpot.makeyourshoppinglist.shared;

import java.io.Serializable;

public class ClientProduct implements Serializable {

	private String product;
	private String id;
	private Boolean visible;
	private String categoryName;

	public ClientProduct() {}
	
	public ClientProduct(String id, String categoryName, String product, Boolean visible) {
		this.id = id;
		this.categoryName = categoryName;
		this.product = product;
		this.visible = visible;
	}

	@Override
	public String toString() {
		return product;
	}

	public String getId() {
		return id;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Boolean getVisible() {
		return visible;
	}

	public String getCategoryName() {
		return categoryName;
	}
}
