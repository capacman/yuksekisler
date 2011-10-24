package com.yuksekisler.domain;

public class YuksekislerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6377129758759945274L;

	public YuksekislerException() {
		super();
	}

	public YuksekislerException(String message, Throwable cause) {
		super(message, cause);
	}

	public YuksekislerException(String message) {
		super(message);
	}

	public YuksekislerException(Throwable cause) {
		super(cause);
	}

}
