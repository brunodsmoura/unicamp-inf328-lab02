package hirondelle.predict.config;

import java.io.IOException;
import java.util.Properties;

public class Configuration {
	
	public static String getProperty(String propertyName) {
		try {
			Properties properties = new Properties();
			properties.load(ClassLoader.getSystemResource("resources/config.properties").openStream());
			return properties.getProperty(propertyName);
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

}
