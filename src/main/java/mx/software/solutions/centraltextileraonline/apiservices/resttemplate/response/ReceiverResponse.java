package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ReceiverResponse {

	@JsonProperty("Rfc")
	private String rfc;

	@JsonProperty("Name")
	private String name;

}
