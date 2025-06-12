package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx;

import lombok.Data;

@Data
public class ShippingPqtxItems {
    private String srvcId;
    private String productIdSAT;
    private String weight;
    private String volL;
    private String volW;
    private String volH;
    private String cont;
    private String qunt;
}
