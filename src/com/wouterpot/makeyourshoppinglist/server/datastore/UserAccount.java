package com.wouterpot.makeyourshoppinglist.server.datastore;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class UserAccount {
	
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String      id;

    @Persistent
	String userId;
	
    @Persistent(mappedBy = "parent", defaultFetchGroup = "true")
    @Element(dependent = "true")
    List<ShoppingList> shoppingLists = new ArrayList<>();

	public UserAccount(String userId) {
		this.userId = userId;
	}
	
	public void addShoppingList(ShoppingList shoppingList) {
		shoppingList.setParent(this);
		shoppingLists.add(shoppingList);
	}
}
