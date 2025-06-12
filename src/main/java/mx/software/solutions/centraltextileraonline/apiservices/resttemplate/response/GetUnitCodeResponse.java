package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetUnitCodeResponse {

	@JsonProperty("ShortName")
	private String shortName;

	@JsonProperty("Name")
	private String name;

	@JsonProperty("Value")
	private String value;

}
