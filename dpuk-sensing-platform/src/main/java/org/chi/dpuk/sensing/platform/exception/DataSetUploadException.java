package org.chi.dpuk.sensing.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "An error occurred whilst uploading the payload.")
public class DataSetUploadException extends RuntimeException {
	private static final long serialVersionUID = -2593526237844270890L;
}