package edu.stanford.bmir.ncbo.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Properties;

/**
 * @author Marcos Martínez-Romero (marcosmartinez@udc.es)
 * @version 06/05/2014 16:58:56
 */
public class PropertiesManager {
	private static String configFile = "src/main/config/config.properties";
	private static String apikeyFile = "src/main/config/apikey";
	private static Properties properties;
	private static String apikey = null;

	static {
		properties = new Properties();
		Properties propertiesApikey = new Properties();
		try {
			properties.load(new FileInputStream(configFile));
			propertiesApikey.load(new FileInputStream(apikeyFile));
			apikey = propertiesApikey.getProperty("apikey");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProperty(String propertyName) {
		return properties.getProperty(propertyName);
	}
	
	public static String getApikey() {
		return apikey;
	}
	
	public static double getPropertyDouble(String propertyName) {
		NumberFormat nf = NumberFormat.getInstance(Locale.US);
		double result=-1;
		try {
			result = nf.parse(properties.getProperty(propertyName)).doubleValue();			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static int getPropertyInt(String propertyName) {
		return Integer.parseInt(properties.getProperty(propertyName));
	}
	
	/*** Test code ***/
	public static void main(String[] args) {
		System.out.println(PropertiesManager.getApikey());
		System.out.println(PropertiesManager.getPropertyDouble("wCoverage"));
	}	
}
