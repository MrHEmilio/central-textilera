package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.PackageRequest;

import java.util.List;


@Data
public class ShipmetBookingResponse {

    private String [] warnings;

    @JsonProperty("status_message")
    private String statusMessage;
    @JsonProperty("shipment_date")
    private String shipmentDate;
    @JsonProperty("shipment_status")
    private String shipmentStatus;
    @JsonProperty("shipment_type")
    private String shipmentType;
    private List<PackageRequest> parcels;
    private String carrier;
    @JsonProperty("carrier_service_code")
    private String carrierServiceCode;
    @JsonProperty("enviaya_service_code")
    private String enviayaServiceCode;
    @JsonProperty("enviaya_shipment_number")
    private String enviayaShipmentNumber;
    private Long id;
    @JsonProperty("carrier_shipment_number")
    private String carrierShipmentNumber;
    @JsonProperty("file_format")
    private  String fileFormat;
    @JsonProperty("label_size")
    private String labelSize;
    @JsonProperty("label_url")
    private String labelUrl;
    @JsonProperty("label_share_link")
    private String labelShareLink;
    private Boolean error;
    private String[] errors;

}
