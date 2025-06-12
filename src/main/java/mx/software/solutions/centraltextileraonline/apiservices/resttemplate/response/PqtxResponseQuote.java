package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.pqtxResponse.BodyResponse;
import lombok.Data;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PqtxResponseQuote {
    //private Object header;
    private BodyResponse body;
}
