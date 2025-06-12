package mx.software.solutions.centraltextileraonline.apiservices.controllers.responses;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientAddressResponse {

	private final UUID id;
	private final String name;
	private final String streetName;
	private final String numExt;
	private final String numInt;
	private final String zipCode;
	private final String suburb;
	private final String municipality;
	private final String state;
	private final String city;
	private final String country;
	private final String latitude;
	private final String longitude;
	private final Boolean predetermined;
	private final Boolean billingAddress;
	private final Boolean active;

}
