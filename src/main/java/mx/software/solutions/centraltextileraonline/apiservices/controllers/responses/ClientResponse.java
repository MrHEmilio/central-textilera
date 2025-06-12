package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class ClientResponse {

	@NonNull
	private final UUID id;
	@NonNull
	private final String name;
	@NonNull
	private final String firstLastname;

	private final String secondLastname;

	@NonNull
	private final CountryCodeResponse countryCode;

	@NonNull
	private final String phone;
	@NonNull
	private final String email;
	@NonNull
	private final Boolean emailValidated;
	@NonNull
	private final Date date;

	private final String rfc;

	private final String companyName;

	private final String fiscalRegimen;

	@NonNull
	private final Boolean active;

}
