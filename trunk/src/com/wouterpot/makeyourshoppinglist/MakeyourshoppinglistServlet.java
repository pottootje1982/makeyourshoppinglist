package com.wouterpot.makeyourshoppinglist;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@SuppressWarnings("serial")
public class MakeyourshoppinglistServlet extends HttpServlet {
	private ShoppingListFactory shoppingListFactory;

	public MakeyourshoppinglistServlet() {}
	
	public void init(ServletConfig config) 
	{
		try {
			super.init(config);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		shoppingListFactory = new ShoppingListFactory();
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
		ShoppingList shoppingList = shoppingListFactory.createShoppingList("https://sites.google.com/site/walterreddock/recepten/zeebrasemfilets-met-kleine-groentes");
		
		for (String category : shoppingList.getCategories()) {
			resp.getWriter().println(category);
			for (Product product : shoppingList.getProducts(category)) {
				resp.getWriter().println(product.getIngredient());				
			}
		}
	}
}
