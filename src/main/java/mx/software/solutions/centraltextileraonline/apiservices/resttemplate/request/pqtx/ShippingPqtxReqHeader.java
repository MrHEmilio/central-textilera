package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx;

import lombok.Data;

@Data
public class ShippingPqtxReqHeader {
    private Security security;
    private Object device = null;
    private Object target = null;
    private Object output = null;
    private Object language = null;
}
