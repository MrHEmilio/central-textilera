package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CrudResponse<T> {

	private final T data;
	private final String message;

}
