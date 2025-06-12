package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request;

import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx.PqtxTokenBody;

@Data
public class PqtxTokenRequest {
    private PqtxTokenBody header;
}
