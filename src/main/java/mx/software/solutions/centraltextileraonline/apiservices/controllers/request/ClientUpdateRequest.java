package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class ClientUpdateRequest {

	@NotNull(message = "client.name.not.null")
	@NotBlank(message = "client.name.not.blank")
	private String name;

	@NotNull(message = "client.first.lastname.not.null")
	@NotBlank(message = "client.first.lastname.not.blank")
	private String firstLastname;

	private String secondLastname;

	@NotNull(message = "client.country.code.not.null")
	private UUID countryCode;

	@Pattern(regexp = "^[0-9]{10}$", message = "client.phone.digit")
	private String phone;

	private String rfc;

	private String companyName;

	private String fiscalRegimen;

}
