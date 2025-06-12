package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ShipmentsRequest {

	@JsonProperty("address_from")
	private AddressRequest addressFrom;

	private List<ParcelRequest> parcels;

	@JsonProperty("address_to")
	private AddressRequest addressTo;

	@JsonProperty("consignment_note_class_code")
	private String consignmentNoteClassCode;

	@JsonProperty("consignment_note_packaging_code")
	private String consignmentNodePackagingCode;

}
