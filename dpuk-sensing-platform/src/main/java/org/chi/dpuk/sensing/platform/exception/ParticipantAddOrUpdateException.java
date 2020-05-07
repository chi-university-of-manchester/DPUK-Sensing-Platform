package org.chi.dpuk.sensing.platform.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Participant cannot be added or updated.")
public class ParticipantAddOrUpdateException extends RuntimeException {
	private static final long serialVersionUID = 3780004006729634992L;
}