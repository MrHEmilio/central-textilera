package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TaxStampResponse {

	@JsonProperty("Uuid")
	private String uuid;

	@JsonProperty("Date")
	private Date date;

	@JsonProperty("CfdiSign")
	private String cfdiSign;

	@JsonProperty("SatCertNumber")
	private String satCertNumber;

	@JsonProperty("SatSign")
	private String satSign;

	@JsonProperty("RfcProvCertif")
	private String rfcProvCertif;

}
