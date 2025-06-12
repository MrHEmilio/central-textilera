package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.pqtxResponse;
import lombok.Data;
@Data
public class ResponseGuide {
    private Boolean success;
    private String messages;
    private String data;
    private String objectDTO;
    private PqtxResponseAdditionalData additionalData;
    private String time;
}
