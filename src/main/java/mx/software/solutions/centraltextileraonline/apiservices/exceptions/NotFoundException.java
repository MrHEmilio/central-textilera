package mx.software.solutions.centraltextileraonline.apiservices.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class NotFoundException extends Exception {

	private static final long serialVersionUID = -7385381074194700818L;

	@NonNull
	private final Controller controller;
	@NonNull
	private final String key;
	private String name;

}
