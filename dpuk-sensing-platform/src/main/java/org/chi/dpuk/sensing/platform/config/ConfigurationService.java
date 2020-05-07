package org.chi.dpuk.sensing.platform.config;

/**
 * Used to configure the system when the application starts.
 *
 */
public interface ConfigurationService {

	/**
	 * Loads any initial data into the database.
	 */
	public void configureDatabase();

	/**
	 * Adds some test users and encounters for development purposes.
	 */
	public void addTestData();
	
}