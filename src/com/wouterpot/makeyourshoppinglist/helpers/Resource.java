package com.wouterpot.makeyourshoppinglist.helpers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.appengine.api.utils.SystemProperty;

public class Resource {
	
    public static boolean isTesting() {
        return SystemProperty.environment.value() == null;
    }
    
	public static String getResource(String path) {
		return isTesting() ? "src/" + path : "WEB-INF/classes/" + path;
	}
	
	public static String[] getResourceListing(Class<?> className, String path)
			throws URISyntaxException, IOException {
		URL dirURL = className.getClassLoader().getResource(path);
		if (dirURL != null && dirURL.getProtocol().equals("file")) {
			/* A file path: easy enough */
			return new File(dirURL.toURI()).list();
		}

		if (dirURL == null) {
			/*
			 * In case of a jar file, we can't actually find a directory. Have
			 * to assume the same jar as className.
			 */
			String me = className.getName().replace(".", "/") + ".class";
			dirURL = className.getClassLoader().getResource(me);
		}

		if (dirURL.getProtocol().equals("jar")) {
			/* A JAR path */
			String jarPath = dirURL.getPath().substring(5,
					dirURL.getPath().indexOf("!")); // strip out only the JAR
													// file
			JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
			Enumeration<JarEntry> entries = jar.entries(); // gives ALL entries
															// in jar
			Set<String> result = new HashSet<String>(); // avoid duplicates in
														// case it is a
														// subdirectory
			while (entries.hasMoreElements()) {
				String name = entries.nextElement().getName();
				if (name.startsWith(path)) { // filter according to the path
					String entry = name.substring(path.length());
					int checkSubdir = entry.indexOf("/");
					if (checkSubdir >= 0) {
						// if it is a subdirectory, we just return the directory
						// name
						entry = entry.substring(0, checkSubdir);
					}
					result.add(entry);
				}
			}
			jar.close();
			return result.toArray(new String[result.size()]);
		}

		throw new UnsupportedOperationException("Cannot list files for URL "
				+ dirURL);
	}
	
	public static List<String> splitBlobByBreak(String html) {
		List<String> resIngredients;
		Document document = Jsoup.parse(html);
		document.select("br").after("\\n");
		document.select("p").after("\\n");
		resIngredients = Arrays.asList(document.text().split("\\s*\\\\n\\s*"));
		return resIngredients;
	}
}
