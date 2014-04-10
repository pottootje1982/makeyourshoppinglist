package com.wouterpot.makeyourshoppinglist.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public final class VoidCallback implements AsyncCallback<Void> {
	@Override
	public void onSuccess(Void result) {
	}

	@Override
	public void onFailure(Throwable caught) {
		caught.printStackTrace();
	}
}