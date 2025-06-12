package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ClientAddressCreateWithoutAccountRequest {

	@NotNull(message = "client.address.street.name.not.null")
	@NotBlank(message = "client.address.street.name.not.blank")
	private String streetName;

	@NotNull(message = "client.address.num.ext.not.null")
	@NotBlank(message = "client.address.num.ext.not.blank")
	private String numExt;

	private String numInt;

	@NotNull(message = "client.address.suburb.not.null")
	private String suburb;

	@NotNull(message = "client.address.zip.code.not.null")
	private String zipCode;

	@NotNull(message = "client.address.municipality.not.null")
	private String municipality;

	@NotNull(message = "client.address.city.not.null")
	private String city;

	@NotNull(message = "client.address.state.not.null")
	private String state;

	@NotNull(message = "client.address.country.not.null")
	private String country;

	private String references;

	@NotNull(message = "client.address.latitude.not.null")
	private String latitude;

	@NotNull(message = "client.address.longitude.not.null")
	private String longitude;

}
