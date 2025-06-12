package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShipmentRequest {

	@JsonProperty("shipment_type")
	private String shipmentType;
	@JsonProperty("parcels")
	private List<PackageRequest> parcels;
	private String content;

}
