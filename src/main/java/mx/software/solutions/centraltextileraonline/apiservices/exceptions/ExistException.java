package mx.software.solutions.centraltextileraonline.apiservices.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;

@Getter
@AllArgsConstructor
public class ExistException extends Exception {

	private static final long serialVersionUID = -1119634946788123963L;

	private final Controller controller;
	private final String key;
	private final String name;

}
