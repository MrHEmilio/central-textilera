package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx;
import lombok.Data;

import java.util.List;

@Data
public class ShipmentDetail {
    private List<Shipment> shipments;
}
