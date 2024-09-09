package be.pirlewiet.digitaal.web.controller;

import static be.occam.utils.spring.web.Controller.response;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import be.occam.utils.spring.web.Result;
import be.occam.utils.spring.web.Result.Value;
import be.pirlewiet.digitaal.domain.exception.ErrorCodes;
import be.pirlewiet.digitaal.domain.exception.PirlewietException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
	
	private final Logger logger
		= LoggerFactory.getLogger( this.getClass() );
	
	@Autowired
	MessageSource messageSource;
	
    @ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleRequestException(IllegalArgumentException e, WebRequest webRequest ) {
		logger.warn("bad request", e);
		return response( e.getMessage(), HttpStatus.BAD_REQUEST );
	}


	@SuppressWarnings("rawtypes")
	@ExceptionHandler(PirlewietException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public Result handlePirlewietException(PirlewietException e, WebRequest webRequest, HttpServletResponse httpServletResponse ) {
		
		logger.warn("pirlewiet exception [{}]", e.getErrorCode().getCode() );
		
		Result result
			= new Result();
		
		result.setValue( Value.NOK );
		result.setErrorCode( e.getErrorCode() );
		
		String message
			= e.getMessage();
		
		if ( message == null ) {
			message = this.messageSource.getMessage( String.format("pwt.error.%s", e.getErrorCode().getCode() ), new Object[] {}, Locale.forLanguageTag("nl-NL") );
		}
		
		result.setMessage( message );
		
		httpServletResponse.setStatus( HttpStatus.OK.value() );
		return result;
		
	}
	
	@SuppressWarnings("rawtypes")
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK)
	public Result handleException(Exception e, WebRequest webRequest, HttpServletResponse httpServletResponse ) {
		
		logger.warn("unexpected error", e);
		
		httpServletResponse.setStatus( HttpStatus.OK.value() );
		
		Result result
			= new Result();
		
		result.setValue( Value.NOK );
		result.setErrorCode( ErrorCodes.INTERNAL );
		result.setMessage( "Er trad een fout op. Probeer AUB opnieuw. Indien het probleem aanhoudt, contacteer dan ons secretariaat." );
		
 		return result;
		
	}
    
}
