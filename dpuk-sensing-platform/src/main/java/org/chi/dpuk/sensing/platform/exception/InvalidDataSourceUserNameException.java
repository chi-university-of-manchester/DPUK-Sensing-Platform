package org.chi.dpuk.sensing.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "User name may only contain alphanumeric characters.")
public class InvalidDataSourceUserNameException extends RuntimeException {
	private static final long serialVersionUID = 8576243054766663567L;
}