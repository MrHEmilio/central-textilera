package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CountryCodeResponse {

	private final UUID id;
	private final String name;
	private final String code;

}
