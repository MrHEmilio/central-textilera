package mx.software.solutions.centraltextileraonline.apiservices.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class DataBaseException extends Exception {

	private static final long serialVersionUID = 1796172107881811286L;

	@NonNull
	private final Controller controller;
	@NonNull
	private final DataBaseActionType dataBaseActionType;
	private String name;

}
