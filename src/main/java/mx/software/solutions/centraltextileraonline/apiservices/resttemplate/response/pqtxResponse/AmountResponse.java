package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.pqtxResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class AmountResponse {
    private BigDecimal shpAmnt;
    private BigDecimal discAmnt;
    private BigDecimal srvcAmnt;
    private BigDecimal subTotlAmnt;
    private BigDecimal taxAmnt;
    private BigDecimal taxRetAmnt;
    private BigDecimal totalAmnt;
}
