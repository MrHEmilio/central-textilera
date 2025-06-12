package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx;

import lombok.Data;

@Data
public class ShippingPqtxBody {
    private ShippingPqtxRequest request;
    private Object response = null;
}
