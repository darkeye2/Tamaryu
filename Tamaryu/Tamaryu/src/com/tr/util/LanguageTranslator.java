package com.tr.util;

import java.util.Locale;
import java.util.ResourceBundle;

public final class LanguageTranslator
{		
	private LanguageTranslator()
	{
		
	}
	public static void changeLanguage(String lang, String country)
	{
		Locale.setDefault(new Locale(lang,country));
		ResourceBundle.clearCache();
	}
	
	public static void changeLanguage(String lang)
	{		
		Locale.setDefault(new Locale(lang));
		ResourceBundle.clearCache();
	}
	
	public static String getString(String key)
	{
		return ResourceBundle.getBundle("localization/Bundle").getString(key);
	}
	
	public static String getCurrentLanguage()
	{
		return Locale.getDefault().toString();
	}
}
