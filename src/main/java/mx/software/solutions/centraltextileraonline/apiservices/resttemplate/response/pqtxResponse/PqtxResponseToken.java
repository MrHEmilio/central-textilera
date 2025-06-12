package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.pqtxResponse;

import lombok.Data;

@Data
public class PqtxResponseToken {
    private boolean success;
    private String messages;
    private PqtxTokenData data;
    private Object objectDTO;
    private String time;


}
