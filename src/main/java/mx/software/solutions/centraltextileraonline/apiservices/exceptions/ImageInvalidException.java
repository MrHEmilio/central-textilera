package mx.software.solutions.centraltextileraonline.apiservices.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;

@Getter
@AllArgsConstructor
public class ImageInvalidException extends Exception {

	private static final long serialVersionUID = 900478284761225290L;

	private final Controller controller;

}
