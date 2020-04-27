package com.sicredi.slc.exception;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class SLCExceptionHandler {

	@ExceptionHandler(NoSuchElementException.class)
	public final ResponseEntity<ExceptionResponse> handleNoSuchElementException(NoSuchElementException ex) {
		return handleException("Recurso não encontrado!", HttpStatus.NOT_FOUND);
	}

	private ResponseEntity<ExceptionResponse> handleException(String message, HttpStatus status) {
		return new ResponseEntity<>(new ExceptionResponse(message), status);
	}
}
