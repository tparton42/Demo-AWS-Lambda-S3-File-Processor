package com.inrhythm;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class PropertyLoader {
	private Properties appProperties;

	private LambdaLogger logger;
	private static final String APP_CONFIG_FILE = "app.properties";

	public PropertyLoader(LambdaLogger lambdaLogger) {
		this.logger = lambdaLogger;

		if (appProperties == null) {
			appProperties = new Properties();
			try {
				// Read properties file from Resources as Stream
				appProperties.load( Thread.currentThread().getContextClassLoader().getResourceAsStream(APP_CONFIG_FILE) );
			} catch (Exception ex) {
				logger.log("Unable to load the config file, " + APP_CONFIG_FILE + ". ");
				logger.log("Exception: " + ex.getMessage());
			}
		}

	}

	public Properties getAppProperties() {
		return appProperties;
	}

}
