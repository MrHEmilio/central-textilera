package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ComplementResponse {

	@JsonProperty("TaxStamp")
	private TaxStampResponse taxStamp;

}
