package com.fasta.app.exceptions;

public class TimeOutException extends RuntimeException {

	@Override
	public String getMessage() {
		return getClass().getName() + " : tempo scaduto";
	}
}
