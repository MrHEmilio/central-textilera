package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx;

import lombok.Data;

@Data
public class PqtxDataCollect {
    private String numbPack;
    private Long planCollDate;
    private Long hourFrom;
    private int hourTo;
    private String guiaNo;
    private Object radGuiaAddrDTOList;
}
