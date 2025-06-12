package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestTemplateExceptionResponse {

	private final String id;
	private final String message;
	private final String messageApi;

}
