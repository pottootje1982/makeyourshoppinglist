package com.wouterpot.makeyourshoppinglist;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

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
		
		Enumeration<String> parameterNames = req.getParameterNames();
		while (parameterNames.hasMoreElements())
		{
			String parameter = parameterNames.nextElement();
			String url = req.getParameter(parameter);
			shoppingListFactory.addToShoppingList(url);
		}
		
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		
		for (String category : shoppingList.getCategories()) {
			resp.getWriter().println();
			resp.getWriter().println(category);
			for (Product product : shoppingList.getProducts(category)) {
				resp.getWriter().println(product.getIngredient());				
			}
		}
		
	}
}
