package com.scb.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TradeException extends ResponseStatusException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5549192821481098555L;

	public TradeException(String message) {
		super(HttpStatus.INTERNAL_SERVER_ERROR,message);
	}
	
	
}
