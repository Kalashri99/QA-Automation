package com.persistent.daisy.gen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class ResourceManager {
	private static Properties properties = new Properties();
	private static FileInputStream input = null;
	private static Map<String, String> map = new LinkedHashMap<String, String>();

	public static void setValue(String key, String value) {
		map.put(key, value);
	}

	public static String getValue(String key) {
		return map.get(key);
	}

	static {
		readProperty();
	}

	public static Properties readProperty() {
		try {
			String path = System.getProperty("user.dir");
			if (path == null) path = ".";

			String file_location = path + File.separator + "config" + File.separator + "HardcodedValues.properties";

			input = new FileInputStream(file_location);
			properties.load(input);

			for (String key : properties.stringPropertyNames()) {
				String value = properties.getProperty(key);
				setValue(key, value);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return properties;
	}
}
		
