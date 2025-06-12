package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SendEmailCfdiResponse {

	@JsonProperty("msj")
	private String message;

	@JsonProperty("success")
	private boolean success;

}
