package com.wouterpot.makeyourshoppinglist.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductDto implements Serializable {

	private String product;
	private List<String> ids = new ArrayList<>();
	private Boolean visible;
	private String categoryName;
	private String aggregatedProductName;

	public ProductDto() {}
	
	public ProductDto(List<String> ids, String aggregatedProductName, String categoryName, String product, Boolean visible) {
		this.ids = ids;
		this.aggregatedProductName = aggregatedProductName;
		this.categoryName = categoryName;
		this.product = product;
		this.visible = visible;
	}

	@Override
	public String toString() {
		return product;
	}

	public List<String> getIds() {
		return ids;
	}
	
	public void addId(String id) {
		ids.add(id);
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

	public String getAggregatedProductName() {
		return aggregatedProductName;
	}
}
