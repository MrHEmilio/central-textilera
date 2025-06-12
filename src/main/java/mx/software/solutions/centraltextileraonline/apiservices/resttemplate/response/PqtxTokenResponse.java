package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.pqtxResponse.PqtxTokenBody;
import lombok.Data;


@Data
public class PqtxTokenResponse {
    private Object header;
    private PqtxTokenBody body;
}
