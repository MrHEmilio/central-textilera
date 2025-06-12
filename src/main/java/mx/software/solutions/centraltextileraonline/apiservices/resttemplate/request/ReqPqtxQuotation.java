package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request;

import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx.Body;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx.Header;

@Data
public class ReqPqtxQuotation {
    private Header header;
    private Body body;
}
