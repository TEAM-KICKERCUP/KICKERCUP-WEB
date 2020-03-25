//----------------------------------------------------------------------------------
//     Created By: Christopher Heid
// Contributor(s): None
//    Description: HTTP Not Found Exception (404)
//----------------------------------------------------------------------------------
package org.kickercup.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception{

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String message){
    	super(message);
    }
}
