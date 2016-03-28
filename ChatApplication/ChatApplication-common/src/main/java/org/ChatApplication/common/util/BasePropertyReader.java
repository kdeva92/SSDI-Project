package org.ChatApplication.common.util;

import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Komal
 *
 */
public abstract class BasePropertyReader {

	public static final String SYSTEM_PROPERTY_IDENTIFIER = "$";

	private static final Logger LOGGER = Logger.getLogger(BasePropertyReader.class);

	private Properties properties = null;

	protected BasePropertyReader(InputStream resourceStream) {
		loadProperties(resourceStream);
	}

	public Properties getProperties() {
		return properties;
	}

	private void loadProperties(InputStream resourceStream) {
		properties = new Properties();
		try {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Trying to read properties from file");
			}

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Resource stream in property reader-" + resourceStream);
			}
			if (resourceStream != null) {
				properties.load(resourceStream);
			} else {
				LOGGER.error("Invalid ResourceStream");
			}
		} catch (IOException e) {
			LOGGER.error("Exception while reading properties from input stream" + e);
		}
	}

	/**
	 * 
	 * @param transactionProperty
	 * @param msgArgs
	 * @return
	 */
	public String getPropertyValue(String propertyName, String... msgArgs) {
		String value = "";
		if (propertyName != null && properties != null) {
			value = (String) properties.get(propertyName);
			if (value != null && value.contains(SYSTEM_PROPERTY_IDENTIFIER)) {
				value = resolveSystemProperty((String) properties.get(propertyName)).trim();

			} else if (value != null && msgArgs != null) {
				value = MessageFormat.format(value, msgArgs);
			}
		}
		return value;
	}

	/**
	 * Method which resolves only system properties. System properties are added
	 * in format ${user.home}
	 * 
	 * @param property
	 * @return
	 */
	private String resolveSystemProperty(String property) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Started resolving property with varaible-" + property);
		}

		if (property.contains("$")) {
			StringBuffer sb = new StringBuffer();
			Pattern pattern = Pattern.compile("\\$\\{(.+)\\}");
			Matcher matcher = pattern.matcher(property);
			while (matcher.find()) {
				String key = matcher.group(1);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Checking system varaibles for property-" + key);
				}
				String val = getSystemPropertyValue(key);
				if (val == null) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Value is null, trying to find reference property" + key);
					}
					// Check reference to other property
					val = getPropertyValue(key);
				}
				if (val != null) {
					matcher.appendReplacement(sb, Matcher.quoteReplacement(val));
				} else {
					LOGGER.error("Failed to resolve key-" + key + " for proprerty-" + property);
				}
			}
			matcher.appendTail(sb);

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Succesfully resolved property to value-" + sb.toString());
			}
			return sb.toString();
		}
		return null;
	}

	/**
	 * Get system env variables
	 * 
	 * @return
	 */
	public static String getSystemPropertyValue(String variable) {

		String osName = System.getProperty("os.name");
		if (osName != null && osName.contains("Windows")) {
			return System.getProperty(variable);
		} else {
			return System.getenv(variable);
		}
	}

}
