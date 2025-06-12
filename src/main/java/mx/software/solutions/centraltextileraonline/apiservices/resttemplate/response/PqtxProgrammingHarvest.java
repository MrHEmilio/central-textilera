package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;

import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.pqtxResponse.PqtxBodyResponseHarvest;

@Data
public class PqtxProgrammingHarvest {
    private Object header;
    private PqtxBodyResponseHarvest body;
}
