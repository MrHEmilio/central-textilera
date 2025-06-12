package mx.software.solutions.centraltextileraonline.apiservices.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class TokenInvalidException extends Exception {

	private static final long serialVersionUID = 900478284761225290L;

	private String token;

}
