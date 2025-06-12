package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request;

import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx.PqtxBodyCollect;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx.ShippingPqtxReqHeader;

@Data
public class PqtxCreateCollect {
    private ShippingPqtxReqHeader header;
    private PqtxBodyCollect body;
}
