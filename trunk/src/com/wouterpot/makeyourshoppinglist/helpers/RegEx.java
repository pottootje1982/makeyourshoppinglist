package com.wouterpot.makeyourshoppinglist.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegEx {

	private static Matcher getMatcher(String input, String regEx) {
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(input);
		return matcher;
	}

	public static String match(String input, String regEx) {
		Matcher matcher = getMatcher(input, regEx);
		return matcher.matches() ? matcher.group() : null;
	}

	public static String find(String input, String regEx) {
		Matcher matcher = getMatcher(input, ".*?(" + regEx + ").*");
		return matcher.matches() ? matcher.group(1) : null;
	}
	
	public static String[] findGroups(String input, String regEx) {
		Matcher matcher = getMatcher(input, ".*?" + regEx + ".*");
		String[] results = new String[matcher.matches() ? matcher.groupCount() : 0];
		for (int i = 0; i < results.length; i++) {
			results[i] = matcher.group(i + 1);
		}
		return results;
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
