package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClothDescriptionResponse {

	private final String name;
	private final boolean isAutomatic;

}
