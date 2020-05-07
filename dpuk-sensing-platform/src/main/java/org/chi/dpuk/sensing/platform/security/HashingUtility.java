package org.chi.dpuk.sensing.platform.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashingUtility {

	public static String hashPassword(String password) {
		int workFactor = 10;
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(workFactor);
		return encoder.encode(password);
	}

	public static boolean doPasswordsMatch(String sourcePassword, String destinationPassword) {
		if (sourcePassword != null && destinationPassword != null) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			return encoder.matches(sourcePassword, destinationPassword);
		}
		return false;
	}

}