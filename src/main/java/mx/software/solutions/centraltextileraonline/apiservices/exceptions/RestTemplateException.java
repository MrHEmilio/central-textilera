package mx.software.solutions.centraltextileraonline.apiservices.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ApiRestTemplate;

@Getter
@AllArgsConstructor
public class RestTemplateException extends Exception {

	private static final long serialVersionUID = 1796172107881811286L;

	@NonNull
	private final ApiRestTemplate apiRestTemplate;
	@NonNull
	private final String key;
	//@NonNull
	private final String message;

}
