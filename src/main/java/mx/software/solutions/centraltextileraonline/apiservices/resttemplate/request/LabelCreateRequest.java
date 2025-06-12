package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LabelCreateRequest {

	@JsonProperty("rate_id")
	private int rateId;

	@JsonProperty("label_format")
	private String labelFormat;

}
