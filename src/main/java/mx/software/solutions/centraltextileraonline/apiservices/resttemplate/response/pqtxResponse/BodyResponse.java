package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.pqtxResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BodyResponse {
    //private Object request;
    private Response response;
}
