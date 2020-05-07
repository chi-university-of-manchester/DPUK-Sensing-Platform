package org.chi.dpuk.sensing.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Data source with the same name cannot be added or updated to the same participant.")
public class DataSourceSameNameException extends RuntimeException {
	private static final long serialVersionUID = 6405464233012570851L;
}