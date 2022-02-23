package com.behl.translator.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Hardik Singh Behl
 * @email behl.hardiksingh@gmail.com
 */

@Getter
@AllArgsConstructor
public enum Language {

	AFRIKAANS("af"), ALBANIAN("sq"), AMHARIC("am"), ARABIC("ar"), ARMENIAN("hy"), AZERBAIJANI("az"), BENGALI("bn"),
	BOSNIAN("bs"), BULGARIAN("bg"), CATALAN("ca"), SIMPLIFIED_CHINESE("zh"), TRADITIONAL_CHINESE("zh-tw"),
	CROATIAN("hr"), CZECH("cs"), DANISH("da"), DARI("fa-AF"), DUTCH("nl"), ENGLISH("en"), ESTONIAN("et"), FARSI("fa"),
	FILIPINO("tl"), FINNISH("fi"), FRENCH("fr"), CANADIAN_FRENCH("fr-CA"), GEORGIAN("ka"), GERMAN("de"), GREEK("el"),
	GUJARATI("gu"), HAITIAN_CREOLE("ht"), HAUSA("ha"), HEBREW("he"), HINDI("hi"), HUNGARIAN("hu"), ICELANDIC("is"),
	INDONESIAN("id'"), IRISH("ga"), ITALIAN("it"), JAPANESE("ja"), KANNADA("kn"), KAZAKH("kk"), KOREAN("ko"),
	LATVIAN("lv"), LITHUANIAN("lt"), MACEDONIAN("mk"), MALAY("ms"), MALAYALAM("ml"), MALTESE("mt"), MARATHI("mr"),
	MONGOLIAN("mn"), NORWEGIAN("no"), PASHTO("ps"), POLISH("pl"), PORTUGUESE("pt"), PUNJABI("pa"), ROMANIAN("ro"),
	RUSSIAN("ru"), SERBIAN("sr'"), SINHALA("si"), SLOVAK("sk"), SLOVENIAN("sl"), SOMALI("so"), SPANISH("es"),
	SWAHILI("sw"), SWEDISH("sv"), TAMIL("ta"), TELUGU("te"), THAI("th"), TURKISH("tr"), UKRAINIAN("uk"), URDU("ur"),
	UZBEK("uz"), VIETNAMESE("vi"), WELSH("cy");

	private final String languageCode;
}
