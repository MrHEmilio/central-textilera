package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LabelCreateResponse {

	@JsonProperty("data")
	private LabelDataCreateResponse labelDataCreateResponse;

}
