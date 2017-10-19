package com.fasta.app.exceptions;

public class TimeOutException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return getClass().getName() + " : tempo scaduto";
	}
}
