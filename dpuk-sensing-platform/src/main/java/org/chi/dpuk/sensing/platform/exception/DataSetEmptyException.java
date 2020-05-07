package org.chi.dpuk.sensing.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "Please select a file to upload.")
public class DataSetEmptyException extends RuntimeException {
	private static final long serialVersionUID = 2734605665431080529L;
}