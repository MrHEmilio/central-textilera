package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request;

import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx.ShippingPqtxBody;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx.ShippingPqtxReqHeader;

@Data
public class ReqPqtxCreateShipping {
    private ShippingPqtxReqHeader header;
    private ShippingPqtxBody body;

}
