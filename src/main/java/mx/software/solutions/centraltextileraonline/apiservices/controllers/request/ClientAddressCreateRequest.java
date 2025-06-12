package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ClientAddressCreateRequest {

	@NotNull(message = "client.address.name.not.null")
	@NotBlank(message = "client.address.name.not.blank")
	private String name;

	@NotNull(message = "client.address.street.name.not.null")
	@NotBlank(message = "client.address.street.name.not.blank")
	private String streetName;

	@NotNull(message = "client.address.num.ext.not.null")
	@NotBlank(message = "client.address.num.ext.not.blank")
	private String numExt;

	private String numInt;

	@NotNull(message = "client.address.suburb.not.null")
	@NotBlank(message = "client.address.suburb.not.blank")
	private String suburb;

	@NotNull(message = "client.address.zip.code.not.null")
	@NotBlank(message = "client.address.zip.code.not.blank")
	private String zipCode;

	@NotNull(message = "client.address.municipality.not.null")
	@NotBlank(message = "client.address.municipality.not.blank")
	private String municipality;

	@NotNull(message = "client.address.state.not.null")
	@NotBlank(message = "client.address.state.not.blank")
	private String state;

	@NotNull(message = "client.address.city.not.null")
	@NotBlank(message = "client.address.city.not.blank")
	private String city;

	@NotNull(message = "client.address.country.not.null")
	@NotBlank(message = "client.address.country.not.blank")
	private String country;

	private String references;

	@NotNull(message = "client.address.latitude.not.null")
	@NotBlank(message = "client.address.latitude.not.blank")
	private String latitude;

	@NotNull(message = "client.address.longitude.not.null")
	@NotBlank(message = "client.address.longitude.not.blank")
	private String longitude;

	@NotNull(message = "client.address.predetermined.not.null")
	private boolean predetermined;

	@NotNull(message = "client.address.billing.address.not.null")
	private boolean billingAddress;

}
