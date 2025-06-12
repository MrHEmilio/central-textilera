package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class EmailNewsletterTemplateCreateRequest {

	@NotNull(message = "email.newsletter.template.name.not.null")
	@NotBlank(message = "email.newsletter.template.name.not.blank")
	private String name;

	@NotNull(message = "email.newsletter.template.subject.not.null")
	@NotBlank(message = "email.newsletter.template.subject.not.blank")
	private String subject;

	@NotNull(message = "email.newsletter.template.content.html.not.null")
	@NotBlank(message = "email.newsletter.template.content.html.not.blank")
	private String contentHtml;

	@NotNull(message = "email.newsletter.template.content.json.not.null")
	@NotBlank(message = "email.newsletter.template.content.json.not.blank")
	private String contentJson;

}
