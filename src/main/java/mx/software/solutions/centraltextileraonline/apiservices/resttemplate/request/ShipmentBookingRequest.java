package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShipmentBookingRequest {
	
	@JsonProperty("enviaya_account")
	private String enviayaAccount;
	@JsonProperty("api_key")
	private String apiKey;
	private String carrier;
	@JsonProperty("carrier_service_code")
	private String carrierServiceCode;
	@JsonProperty("origin_direction")
	private DirectionRequest originDirection;
	@JsonProperty("destination_direction")
	private DirectionRequest destinationDirection;
	private ShipmentRequest shipment;
	@JsonProperty("file_format")
	private String fileFormat;
	
	

}
