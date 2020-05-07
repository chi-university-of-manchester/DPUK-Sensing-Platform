package org.chi.dpuk.sensing.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "If a contact number is provided, it must be valid.")
public class InvalidContactNumberException extends RuntimeException {
	private static final long serialVersionUID = 3780004006729634992L;
}