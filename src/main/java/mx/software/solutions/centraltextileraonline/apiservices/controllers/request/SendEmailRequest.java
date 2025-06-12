package mx.software.solutions.centraltextileraonline.apiservices.controllers.request;

import lombok.Data;

@Data
public class SendEmailRequest {

	private String subject;
	private String content;
	private String[] sendTo;


}
