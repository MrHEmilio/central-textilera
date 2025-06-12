package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.SendEmailNewsletterTemplate;

@Data
public class EmailNewsletterTemplateSendRequest {

	@NotNull(message = "email.newsletter.template.id.not.null")
	private UUID emailNewsletterTemplate;

	@NotNull(message = "email.newsletter.template.send.not.null")
	private SendEmailNewsletterTemplate sendTo;

}
