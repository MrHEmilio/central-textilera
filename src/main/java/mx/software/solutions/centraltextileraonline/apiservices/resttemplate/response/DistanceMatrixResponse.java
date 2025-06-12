package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DistanceMatrixResponse {

	@JsonProperty("origin_address")
	private List<String> originAddress;

	@JsonProperty("destination_address")
	private List<String> destinationAddress;

	private List<RowResponse> rows;

	private String status;

	@JsonProperty("error_message")
	private String errorMessage;

}
