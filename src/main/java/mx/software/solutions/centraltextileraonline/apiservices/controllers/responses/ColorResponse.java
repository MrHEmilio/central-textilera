package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class ColorResponse {

	private final UUID id;
	private final String name;
	private final String code;
	@NonNull
	private final Boolean active;

}
