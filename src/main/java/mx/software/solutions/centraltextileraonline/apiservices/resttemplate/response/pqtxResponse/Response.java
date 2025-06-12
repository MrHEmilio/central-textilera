package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.pqtxResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    private boolean success;
    private DataResponse data;
    private String time;
    //private Object objectDTO;
    //private List<String> messages;
}
