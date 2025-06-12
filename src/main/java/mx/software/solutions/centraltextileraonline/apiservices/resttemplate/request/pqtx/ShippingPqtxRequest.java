package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx;

import java.util.List;

@lombok.Data
public class ShippingPqtxRequest {
    private List<ShippingPqtxData> data;
    private Object objectDTO;
}
