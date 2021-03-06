package com.wouterpot.makeyourshoppinglist.client.handlers;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;

public final class HyperlinkHandler implements ClickHandler {
	private String site;

	public HyperlinkHandler(String site) {
		this.site = site;
	}

	@Override
	public void onClick(ClickEvent event) {
		Window.open(site, "_blank", "");
	}
}