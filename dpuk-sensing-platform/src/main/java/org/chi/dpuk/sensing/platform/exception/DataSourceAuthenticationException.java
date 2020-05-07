package org.chi.dpuk.sensing.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Invalid Data Source Credentials.")
public class DataSourceAuthenticationException extends RuntimeException {
	private static final long serialVersionUID = -7737768609815535180L;
}