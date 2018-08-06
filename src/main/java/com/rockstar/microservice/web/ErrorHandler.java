package com.rockstar.microservice.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.rockstar.microservice.service.NotFoundException;
import com.rockstar.microservice.service.NotUniqueException;

@ControllerAdvice
public class ErrorHandler {

	@Autowired private MessageSource messageSource;
	
	public ErrorHandler() {
	}
	
	@ExceptionHandler({HttpMediaTypeNotSupportedException.class})
	@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
	public @ResponseBody ErrorResource handleMediaTypeNotAcceptableException(HttpMediaTypeNotSupportedException mediatypenotsupportedex) {
		ErrorResource error = new ErrorResource();
		error.setError("unsupported_mediatype");
		error.setDescription(mediatypenotsupportedex.getMessage());
		return error;
	}
	
	@ExceptionHandler({NotFoundException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public @ResponseBody ErrorResource handleNotFound(NotFoundException notfoundex) {
		ErrorResource error = new ErrorResource();
		error.setError("not_found");
		error.setDescription(notfoundex.getMessage());
		return error;
	}
	
	@ExceptionHandler({NotUniqueException.class})
	@ResponseStatus(HttpStatus.CONFLICT)
	public @ResponseBody ErrorResource handleNotFound(NotUniqueException notuniqueex) {
		ErrorResource error = new ErrorResource();
		error.setError("not_unique");
		error.setDescription(notuniqueex.getMessage());
		return error;
	}
	
	@ExceptionHandler({MethodArgumentNotValidException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorResource handleNotValid(MethodArgumentNotValidException notvalidex) {
		String fieldErrorCode = null;
		List<String> errorMessages = null;
		BindingResult validationResult = notvalidex.getBindingResult();
		List<FieldError> fieldErrors = validationResult.getFieldErrors();
		ErrorResource error = new ErrorResource();
		
		error.setError("validation_error");
		error.setDescription("invalid input");
		
		if (fieldErrors != null && !fieldErrors.isEmpty()) {
			errorMessages = new ArrayList<String>();
			
			for (FieldError currentFieldError : fieldErrors) {
				fieldErrorCode = String.format("%s.%s.%s", currentFieldError.getCode(), currentFieldError.getObjectName(), currentFieldError.getField());
				errorMessages.add(this.getText(fieldErrorCode, currentFieldError.getArguments()));
			}
			error.setMessages(errorMessages);
		}
		return error;
	}
	
	@ExceptionHandler({RuntimeException.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody ErrorResource handleInternalServer(Exception exception) {
		ErrorResource error = new ErrorResource();
		error.setError("internal_error");
		error.setDescription(exception.getMessage());
		exception.printStackTrace();
		return error;
	}
	
	@ExceptionHandler({HttpMessageNotReadableException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorResource handleMessageNotReadableException(HttpMessageNotReadableException messagenotreadableex) {
		ErrorResource error = new ErrorResource();
		error.setError("unreadable_message");
		error.setDescription(messagenotreadableex.getMessage());
			
		return error;
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorResource handleMissingRequestParameterException(MissingServletRequestParameterException missingparameterex) {
		ErrorResource errorDto = new ErrorResource();
		errorDto.setError("missing_parameter");
		errorDto.setDescription(missingparameterex.getMessage());
		return errorDto;
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public @ResponseBody ErrorResource handleRequestBindingException(ServletRequestBindingException requestbindingex, HttpServletResponse response) throws IOException {
		ErrorResource error = new ErrorResource();
		error.setError("invalid_parameter");
		error.setDescription(requestbindingex.getMessage());
		return error;
	}
	
	private String getText(String error, Object...arguments) {
		return this.messageSource.getMessage(error, arguments, LocaleContextHolder.getLocale());
	}
}

