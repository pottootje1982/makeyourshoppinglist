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
	  input = input.replaceAll("[aàáâãäåæ]", "a");
	  input = input.replaceAll("[cç]", "c");
	  input = input.replaceAll("[eèéêëæ]", "e");
	  input = input.replaceAll("[iìíîï]", "i");
	  input = input.replaceAll("[nñ]", "n");
	  input = input.replaceAll("[oòóôõöø]", "o");
	  input = input.replaceAll("[sß]", "s");
	  input = input.replaceAll("[uùúûü]", "u");
	  input = input.replaceAll("[yÿ]", "y");
	  return input;
	}

}
