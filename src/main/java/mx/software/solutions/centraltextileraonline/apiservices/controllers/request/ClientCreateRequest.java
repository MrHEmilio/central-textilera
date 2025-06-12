package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class ClientCreateRequest {

	@NotNull(message = "client.name.not.null")
	@NotBlank(message = "client.name.not.blank")
	private String name;

	@NotNull(message = "client.first.lastname.not.null")
	@NotBlank(message = "client.first.lastname.not.blank")
	private String firstLastname;

	private String secondLastname;

	@NotNull(message = "client.country.code.not.null")
	private UUID countryCode;

	@NotNull(message = "client.phone.not.null")
	@NotBlank(message = "client.phone.not.blank")
	@Pattern(regexp = "^[0-9]{10}$", message = "client.phone.digit")
	private String phone;

	@NotNull(message = "client.email.not.null")
	@NotBlank(message = "client.email.not.blank")
	@Email(message = "client.email.not.valid")
	private String email;

	@NotNull(message = "client.password.not.null")
	@NotBlank(message = "client.password.not.blank")
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "client.password.not.valid")
	private String password;

	private String rfc;

	private String companyName;

	private String fiscalRegimen;

}
