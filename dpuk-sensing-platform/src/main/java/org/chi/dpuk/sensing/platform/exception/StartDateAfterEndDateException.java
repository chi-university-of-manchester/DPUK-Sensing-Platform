package org.chi.dpuk.sensing.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The start date cannot be in the future.")
public class StartDateAfterEndDateException extends RuntimeException {
	private static final long serialVersionUID = -1183080551486438157L;
}