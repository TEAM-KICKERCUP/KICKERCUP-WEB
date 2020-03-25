//----------------------------------------------------------------------------------
//     Created By: Christopher Heid
// Contributor(s): Jonas Jahns (Minor changes)
//    Description: Recieving and mapping of exceptions
//----------------------------------------------------------------------------------
package org.kickercup.api.exception;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.itextpdf.text.DocumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import twitter4j.TwitterException;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class.getName());

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({TwitterException.class, IOException.class, DocumentException.class, InstantiationException.class})
	public ResponseEntity<?> criticalServerExceptionHandler(Exception ex, WebRequest request) {
		LOGGER.log(Level.SEVERE, String.join(", ", new String[]{ex.getMessage(), request.getDescription(false)}));
		return new ResponseEntity<>(new ErrorDetails(new Date(), "An error occurred", request.getDescription(false)),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globleExcpetionHandler(Exception ex, WebRequest request) {
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
