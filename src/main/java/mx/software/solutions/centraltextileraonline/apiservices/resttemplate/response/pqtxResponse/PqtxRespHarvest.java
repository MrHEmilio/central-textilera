package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.pqtxResponse;

import lombok.Data;

import java.util.List;

@Data
public class PqtxRespHarvest {
    private Boolean success;
    private List<PqtxResponseMessges> messages;
    private Object data;
    private Object objectDTO;
    private String time;
}
