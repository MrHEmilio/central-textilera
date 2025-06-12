package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetFiscalRegimensResponse {

	@JsonProperty("Natural")
	private boolean natural;

	@JsonProperty("Moral")
	private boolean moral;

	@JsonProperty("Name")
	private String name;

	@JsonProperty("Value")
	private String value;

}
