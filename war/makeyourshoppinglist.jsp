<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="com.wouterpot.makeyourshoppinglist.*" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>

  <body>

	<table><tr><td>
<%
		ShoppingListFactory shoppingListFactory = new ShoppingListFactory();
		Enumeration parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements())
		{
			String parameter = (String)parameterNames.nextElement();
			String url = request.getParameter(parameter);
			shoppingListFactory.addToShoppingList(url);
		}
		
		ShoppingList shoppingList = shoppingListFactory.getShoppingList();
		if (shoppingList.isEmpty()) { %>Shopping list is empty. No ingredients could be found in specified URLs.<%}
		
		for (String category : shoppingList.getCategories()) {
			if (category.equals("supermarket")) { %></td><td><% }
			%>
			<b><%=category %></b><ul>
			<%
			for (Product product : shoppingList.getProducts(category)) {
				%>
				<li><%=product.getIngredient() %></li>
				<%
			}
			%>
			</ul>
		<%
		}
		%>

	</td></tr></table>
  </body>
</html>