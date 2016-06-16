package com.tr.examplecode;

import com.tr.util.LanguageTranslator;

public class getTranslatedString
{
	//String (example "tamaryu") has to be a Key in ALL Bundle.properties Files (at com.tr.localization)
	//LanguageTranslator is static
	
	String translation = LanguageTranslator.getString("tamaryu"); 

}
