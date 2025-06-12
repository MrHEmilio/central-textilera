package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Data
public class ShippingPqtxData {

    private String billRad = "REQUEST";
    private String billClntId;
    private String pymtMode = "PAID";
    private String pymtType = "C";
    private String comt = "Na";
    private List<ShippingPqtxAddr> radGuiaAddrDTOList;
    private List<ShippingPqtxItems> radSrvcItemDTOList;
    private List<ShippingPqtxItemDto> listSrvcItemDTO;
    private String typeSrvcId;
    private List<ShippingPqtxRef> listRefs;
}

