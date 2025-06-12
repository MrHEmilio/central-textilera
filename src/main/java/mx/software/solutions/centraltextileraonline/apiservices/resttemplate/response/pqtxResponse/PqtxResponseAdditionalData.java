package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.pqtxResponse;
import lombok.Data;
import java.math.BigDecimal;
@Data
public class PqtxResponseAdditionalData {
    Long creditAmnt;
    BigDecimal subTotlAmnt;
    BigDecimal totalAmnt;
}
