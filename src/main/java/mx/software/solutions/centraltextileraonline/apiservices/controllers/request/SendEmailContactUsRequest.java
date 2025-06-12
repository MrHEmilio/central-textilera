package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class SendEmailContactUsRequest {

	@NotNull(message = "send.email.contact.us.name.not.null")
	@NotBlank(message = "send.email.contact.us.name.not.blank")
	private String name;

	@NotNull(message = "send.email.contact.us.email.not.null")
	@NotBlank(message = "send.email.contact.us.email.not.blank")
	@Email(message = "send.email.contact.us.email.not.valid")
	private String email;

	@Pattern(regexp = "^[0-9]{10}$", message = "send.email.contact.us.phone.digit")
	private String phone;

	@NotNull(message = "send.email.contact.us.name.not.null")
	@NotBlank(message = "send.email.contact.us.name.not.blank")
	private String message;

}
