package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx;

import java.math.BigDecimal;
import java.util.List;

@lombok.Data
public class Data {
    private ShipmentDetail shipmentDetail;
    private ClientAddr clientAddrDest;
    private ClientAddr clientAddrOrig;
    private Services services =  new Services();
    private List<String> quoteServices = List.of("ALL");
}

@lombok.Data
class Services {
    private String dlvyType = "1";
    private String ackType = "N";
    private BigDecimal totlDeclVlue = new BigDecimal(0);
    private String invType = "N";
    private String radType = "1";
}
