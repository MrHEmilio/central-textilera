package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RateRequest {
	
	@JsonProperty("enviaya_account")
	private String enviayaAccount;
	@JsonProperty("api_key")
	private String apiKey;
	private ShipmentRequest shipment;
	@JsonProperty("origin_direction")
	private DirectionRequest originDirection;
	@JsonProperty("destination_direction")
	private DirectionRequest destinationDirection;
	

}
