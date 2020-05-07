package org.chi.dpuk.sensing.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Data set should be unique within a study.")
public class DataSetNotUniqueWithinAStudyException extends RuntimeException {
	private static final long serialVersionUID = 1689152585571929297L;
}