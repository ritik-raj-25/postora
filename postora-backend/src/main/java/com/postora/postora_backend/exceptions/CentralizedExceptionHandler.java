package com.postora.postora_backend.exceptions;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class CentralizedExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(UserNotFoundException.class)
	public  ResponseEntity<ErrorDetails> handleUserNotFoundException(
			UserNotFoundException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND); 
	}
	
	@ExceptionHandler(PostNotFoundException.class)
	public  ResponseEntity<ErrorDetails> handlePostNotFoundException(
			PostNotFoundException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND); 
	}
	
	@ExceptionHandler(CommentNotFoundException.class)
	public  ResponseEntity<ErrorDetails> handleCommentNotFoundException(
			CommentNotFoundException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND); 
	}
	
	@ExceptionHandler(LikeNotFoundException.class)
	public  ResponseEntity<ErrorDetails> handleLikeNotFoundException(
			LikeNotFoundException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND); 
	}
	
	@ExceptionHandler(BookmarkNotFoundException.class)
	public  ResponseEntity<ErrorDetails> handleBookmarkNotFoundException(
			BookmarkNotFoundException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND); 
	}
	
	@ExceptionHandler(UnauthorizedActionException.class)
	public  ResponseEntity<ErrorDetails> handleUnauthorizedActionException(
			UnauthorizedActionException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.UNAUTHORIZED); 
	}
	
	@ExceptionHandler(InvalidFileTypeException.class)
	public  ResponseEntity<ErrorDetails> handleInvalidFileTypeException(
			InvalidFileTypeException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.UNPROCESSABLE_ENTITY); 
	}
	
	@ExceptionHandler(IOException.class)
	public  ResponseEntity<ErrorDetails> handleIOException(
			IOException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR); 
	}
	
	@ExceptionHandler(MultipartException.class)
	public  ResponseEntity<ErrorDetails> handleMultipartException(
			MultipartException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST); 
	}
	
	@ExceptionHandler(UserAlreadyExistException.class)
	public  ResponseEntity<ErrorDetails> handleUserAlreadyExistException(
			UserAlreadyExistException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.CONFLICT); 
	}
	
	@ExceptionHandler(PostAlreadyLikedException.class)
	public  ResponseEntity<ErrorDetails> handlePostAlreadyLikedException(
			PostAlreadyLikedException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.CONFLICT); 
	}
	
	@ExceptionHandler(PostAlreadyBookmarkedException.class)
	public  ResponseEntity<ErrorDetails> handlePostAlreadyBookmarkedException(
			PostAlreadyBookmarkedException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.CONFLICT); 
	}
	
	@ExceptionHandler(CategoryAlreadyExistException.class)
	public  ResponseEntity<ErrorDetails> handleCategoryAlreadyExistException(
			CategoryAlreadyExistException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.CONFLICT); 
	}
	
	@ExceptionHandler(CategoryNotFoundException.class)
	public  ResponseEntity<ErrorDetails> handleCategoryNotFoundException(
			CategoryNotFoundException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND); 
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public  ResponseEntity<ErrorDetails> handleConstraintViolationException(
			ConstraintViolationException ex, WebRequest request) {
		
		String message = ex.getConstraintViolations().stream()
													 .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
													 .collect(Collectors.joining("; "));
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), message, request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.BAD_REQUEST); 
	}
	
	@ExceptionHandler(TagNotFoundException.class)
	public  ResponseEntity<ErrorDetails> handleTagNotFoundException(
			TagNotFoundException ex, WebRequest request) {
		
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND); 
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		
		String message = ex.getFieldErrors().stream()
										    .map(error -> error.getField() + " : " + error.getDefaultMessage())
										    .collect(Collectors.joining("; "));
		ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), message, request.getDescription(false));
		return new ResponseEntity<Object>(errorDetails, HttpStatus.BAD_REQUEST); 
	}
	
}
