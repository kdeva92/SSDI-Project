package org.ChatApplication.ui.service.utilities;

import org.ChatApplication.common.util.BasePropertyReader;

/**
 * 
 * @author Komal
 *
 */
public class PropertyReader extends BasePropertyReader {

	private static PropertyReader instance = new PropertyReader();
	private static String fileName = "resources/config.properties";

	public static PropertyReader getInstance() {
		return instance;
	}

	protected PropertyReader() {
		super(PropertyReader.class.getClassLoader().getResourceAsStream(fileName));
		// TODO Auto-generated constructor stub
	}
}
