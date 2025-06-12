package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetProductCodeResponse {

	@JsonProperty("DangerousMaterial")
	private String dangerousMaterial;

	@JsonProperty("Complement")
	private String complement;

	@JsonProperty("Name")
	private String name;

	@JsonProperty("Value")
	private String value;

}
