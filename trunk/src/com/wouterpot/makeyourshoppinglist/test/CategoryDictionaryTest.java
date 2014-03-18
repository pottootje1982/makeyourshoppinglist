package com.wouterpot.makeyourshoppinglist.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.tools.ant.launch.Launcher;
import org.junit.Test;

import com.wouterpot.makeyourshoppinglist.CategoryDictionary;

public class CategoryDictionaryTest {

	private ArrayList<String> getFiles(String path) throws IOException
	{
		final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
	
		ArrayList<String> files = new ArrayList<String>();
		if(jarFile.isFile()) {  // Run with JAR file
		    final JarFile jar = new JarFile(jarFile);
		    final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
		    while(entries.hasMoreElements()) {
		        final String name = entries.nextElement().getName();
		        if (name.startsWith(path + "/")) { //filter according to the path
		        	files.add(name);
		        }
		    }
		    jar.close();
		} else { // Run with IDE
		    final URL url = Launcher.class.getResource("/" + path);
		    if (url != null) {
		        try {
		            final File apps = new File(url.toURI());
		            for (File app : apps.listFiles()) {
		            	files.add(app.getAbsolutePath());
		            }
		        } catch (URISyntaxException ex) {
		            // never happens
		        }
		    }
		}
		return files;
	}
	
	@Test
	public void testGetFiles() throws IOException
	{
		ArrayList<String> files = getFiles("data/en");
		files.size();
	}
	
	@Test
	public void testGetCategory() {
		CategoryDictionary categoryDictionary = new CategoryDictionary("en", "/data");
		String category = categoryDictionary.getCategory("bread");
		assertEquals("baker", category);
	}

	@Test
	public void openResourceTest() throws IOException {
		InputStream resourceAsStream = getClass().getResourceAsStream("/META-inf/jdoconfig.xml");
		resourceAsStream = getClass().getResourceAsStream("/data/en/baker.txt");
		resourceAsStream.close();
	}
}
