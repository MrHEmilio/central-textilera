package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.pqtxResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DataResponse {
    private String clientId;
    private String clientDest;
    private String clntClasifTarif;
    private String agreementType;
    private String pymtMode;
    private AdrOrgDest clientAddrOrig;
    private AdrOrgDest clientAddrDest;
    private List<String>  quoteServices;
    private List<QuotationResponse> quotations;
}
