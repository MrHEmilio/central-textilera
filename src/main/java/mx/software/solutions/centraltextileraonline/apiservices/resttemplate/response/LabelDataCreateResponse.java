package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LabelDataCreateResponse {

	@JsonProperty("id")
	private String id;

	@JsonProperty("type")
	private String type;

	@JsonProperty("attributes")
	private LabelAttributesResponse labelAttributesResponse;

}
