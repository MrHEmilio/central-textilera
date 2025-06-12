package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LabelAttributesResponse {

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("update_at")
	private String updateAt;

	@JsonProperty("status")
	private String status;

	@JsonProperty("tracking_number")
	private String tracingNumber;

	@JsonProperty("tracking_status")
	private String trackingStatus;

	@JsonProperty("label_url")
	private String labelUrl;

	@JsonProperty("tracking_url_provider")
	private String trackingUrlProvider;

	@JsonProperty("rate_id")
	private String rateId;

}
