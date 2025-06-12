package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class AdminResponse {

	@NonNull
	private final UUID id;
	@NonNull
	private final String name;
	@NonNull
	private final String firstLastname;

	private final String secondLastname;
	@NonNull
	private final String email;
	@NonNull
	private final Boolean root;
	@NonNull
	private final Date date;
	@NonNull
	private final Boolean active;

}
