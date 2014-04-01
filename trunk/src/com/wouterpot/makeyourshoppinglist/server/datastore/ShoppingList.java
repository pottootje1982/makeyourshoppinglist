package com.wouterpot.makeyourshoppinglist.server.datastore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.jdo.JDOException;
import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import ch.lambdaj.group.Group;
import static ch.lambdaj.Lambda.*;

import com.google.appengine.labs.repackaged.com.google.common.base.Strings;
import com.wouterpot.makeyourshoppinglist.config.CategoryDictionary;
import com.wouterpot.makeyourshoppinglist.config.LanguageDictionary;
import com.wouterpot.makeyourshoppinglist.config.ProductInfo;
import com.wouterpot.makeyourshoppinglist.helpers.RegEx;
import com.wouterpot.makeyourshoppinglist.server.PMF;
import com.wouterpot.makeyourshoppinglist.server.ShoppingListFactory;
import com.wouterpot.makeyourshoppinglist.shared.ProductDto;
import com.wouterpot.makeyourshoppinglist.shared.ShoppingListDto;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class ShoppingList {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @Extension(vendorName = "datanucleus", key = "gae.encoded-pk", value = "true")
    private String      id;
	
    @Persistent(mappedBy = "parent", defaultFetchGroup = "true")
    List<Category> categoriesToProducts = new ArrayList<Category>();
    
    @Persistent(defaultFetchGroup = "true")
    ArrayList<String> sites = new ArrayList<String>();
        
	private LanguageDictionary languageDictionary;

	public ShoppingList(LanguageDictionary languageDictionary) {
		this.languageDictionary = languageDictionary;
	}

	public void addIngredients(String recipeId, List<String> ingredients, String language) {
		if (!sites.contains(recipeId)) {
			
			CategoryDictionary categoryDictionary = languageDictionary.getCategoryDictionary(language);
			for (String ingredient : ingredients) {
				createProduct(categoryDictionary, ingredient);
			}
			sites.add(recipeId);
		
			PMF.open();
			PMF.begin();
			
			PMF.makePersistent(this);
						
			PMF.commit();
			PMF.close();
		}
	}

	public void createProduct(CategoryDictionary categoryDictionary, String ingredientString) {
		ingredientString = RegEx.escapeStrangeChars(ingredientString);
		ingredientString = ingredientString.trim();
		if (Strings.isNullOrEmpty(ingredientString)) return;
		ProductInfo productInfo = categoryDictionary.getProductInfo(ingredientString);
		Category category = getOrCreateCategory(productInfo != null ? productInfo.getCategory() : Product.DEFAULT_CATEGORY);
		
		Product product = new Product(productInfo);
		Ingredient ingredient = new Ingredient(ingredientString);
		category.addProduct(product);
		product.setIngredient(ingredient);
	}
	
	private Category getCategory(String categoryName) {
		for (Category category : getCategories()) {
			if (category.equals(categoryName))
				return category;
		}
		return null;
	}

	private Category getOrCreateCategory(String categoryName) {
		Category category = getCategory(categoryName);
		if (category == null) {
			category = new Category(categoryName);
			category.setParent(this);
			getCategories().add(category);
		}
		return category;
	}

	public List<Category> getCategories() {
		Collections.sort(categoriesToProducts);
		return categoriesToProducts;
	}

	public List<Product> getProducts(String categoryName) {
		Category category = getCategory(categoryName);
		return category != null ? category.getProducts() : null;
	}
	
	public void setLanguageDictionary(LanguageDictionary languageDictionary) {
		this.languageDictionary = languageDictionary;
	}

	public static ShoppingListDto getShoppingList() {
		Map<String, ArrayList<ProductDto>> shoppingListMap = new TreeMap<String, ArrayList<ProductDto>>();
		ArrayList<String> sites = new ArrayList<String>();
		ShoppingListFactory shoppingListFactory = ShoppingListFactory.get();
		
		if (shoppingListFactory.hasValidShoppingList()) {
			try {
				PMF.open();
				PMF.begin();
				
				ShoppingList shoppingList = PMF.getObjectById(ShoppingList.class, shoppingListFactory.getShoppingList().getId());
		
				for (String site : shoppingList.sites)
					sites.add(site);
				List<Category> categories = shoppingList.getCategories();
				for (Category category : categories) {
					ArrayList<ProductDto> productStrings = new ArrayList<ProductDto>();
					shoppingListMap.put(category.getCategoryName(), productStrings);
					List<Product> products = category.getProducts();
					Group<Product> groups = group(products, by(on(Product.class).getProductKey()));
					List<Product> uniqueProducts = new ArrayList<>();
					for (String productKey : groups.keySet()) {
						List<Product> sameProducts = groups.find(productKey);
						uniqueProducts.add(aggregate(sameProducts, new ProductAggregator()));
					}
					for (Product product : uniqueProducts) {
						productStrings.add(new ProductDto(product.getIds(), product.getAggregatedProductName(), category.getCategoryName(), product.toString(), product.getVisible()));
					}
				}
				
				PMF.commit();
			}
			catch (JDOException ex) {
				ex.printStackTrace();
			}
			finally {
				PMF.rollback();
				PMF.close();
			}
		}
		
		return new ShoppingListDto(shoppingListMap, sites);
	}

	public boolean isEmpty() {
		return getCategories().isEmpty();
	}

	public List<Product> getProduct(String categoryName, List<String> ids) {
		Category category = getCategory(categoryName);
		List<Product> products = category.getProducts();
		List<Product> results = new ArrayList<>();
		for (Product product : products) {
			if (ids.contains(product.getId())) {
				results.add(product);
			}
		}
		return results;
	}

	public String getId() {
		return id;
	}
}
