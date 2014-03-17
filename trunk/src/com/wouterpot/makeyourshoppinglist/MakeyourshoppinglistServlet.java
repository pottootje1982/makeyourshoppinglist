package com.wouterpot.makeyourshoppinglist;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@SuppressWarnings("serial")
public class MakeyourshoppinglistServlet extends HttpServlet {
	public MakeyourshoppinglistServlet()
	{
	}
	
	public void init(ServletConfig config) 
	{
		try {
			super.init(config);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IngredientsScraper ingredientsScraper = new IngredientsScraper(getServletContext());
		
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
		Document doc = Jsoup.connect("http://en.wikipedia.org/").get();
		Elements newsHeadlines = doc.select("#mp-itn b a");
		for (Element element : newsHeadlines) {
			resp.getWriter().println(element.text());
		}
		

		
	}
}
