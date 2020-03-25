//----------------------------------------------------------------------------------
//     Created By: Christopher Heid
// Contributor(s): None
//    Description: HTTP Conflict Exception
//----------------------------------------------------------------------------------
package org.kickercup.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class RessourceValueViolationException extends Exception{

	private static final long serialVersionUID = 2L;

	public RessourceValueViolationException(String message){
    	super(message);
    }
}
