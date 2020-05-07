package org.chi.dpuk.sensing.platform.config;

import java.util.Properties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

/**
 * This class manages the build settings.
 */
@Component
public class BuildSettings {
	
	private final String BUILD_SETTINGS_FILE = "/build.properties";
	
	private boolean localDevelopment = false;
	private boolean useHibernate = false;

	private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

	// Singleton: create private instance
	private static class SingletonHolder {
		private static final BuildSettings buildSettings = new BuildSettings();
	}

	// Singleton: Prevent other classes from creating an instance.
	private BuildSettings() {
		loadBuildSettings();
	}

	// Singleton: Make private instance accessible.
	public static BuildSettings getInstance() {
		return SingletonHolder.buildSettings;
	}

	private void loadBuildSettings() {
		try {
			Resource resource = new ClassPathResource(BUILD_SETTINGS_FILE);
			Properties props = PropertiesLoaderUtils.loadProperties(resource);

			String propertyString = props.getProperty("environment.localdevelopment");
			localDevelopment = propertyString.equals("true") ? true : false;

			propertyString = props.getProperty("database.usehibernate");
			useHibernate = propertyString.equals("true") ? true : false;
		} catch (Exception exception) {
			log.error("Error reading build settings file: " + BUILD_SETTINGS_FILE);
		}
	}

	public boolean getLocalDevelopment() {
		return localDevelopment;
	}

	public boolean getUseHibernate() {
		return useHibernate;
	}

}