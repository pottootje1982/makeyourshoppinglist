package com.wouterpot.makeyourshoppinglist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegEx {

	public static String match(String input, String regEx) {
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(input);
		return matcher.matches() ? matcher.group() : null;
	}

	public static String find(String input, String regEx) {
		Pattern pattern = Pattern.compile(".*(" + regEx + ").*");
		Matcher matcher = pattern.matcher(input);
		return matcher.matches() ? matcher.group(1) : null;
	}
	
	public static String escapeStrangeChars(String input)
	{
	  input = input.toLowerCase();
	  input = input.replaceAll("[a�������]", "a");
	  input = input.replaceAll("[c�]", "c");
	  input = input.replaceAll("[e�����]", "e");
	  input = input.replaceAll("[i����]", "i");
	  input = input.replaceAll("[n�]", "n");
	  input = input.replaceAll("[o������]", "o");
	  input = input.replaceAll("[s�]", "s");
	  input = input.replaceAll("[u����]", "u");
	  input = input.replaceAll("[y�]", "y");
	  return input;
	}

}
