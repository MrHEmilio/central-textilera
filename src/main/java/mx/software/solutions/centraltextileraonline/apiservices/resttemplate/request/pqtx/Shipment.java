package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Shipment {
    private long sequence;
    private int quantity;
    private String shpCode;
    private BigDecimal weight;
    private BigDecimal volume;
    private BigDecimal longShip;
    private BigDecimal widthShip;
    private BigDecimal highShip;
}
