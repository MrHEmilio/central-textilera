package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx;
import lombok.Data;

@Data
public class Header {
    private Security security;
    private Target target = new Target();
    private String output = "JSON";
}
@Data
class Target {
    private String module = "QUOTER";
    private String version = "1.0";
    private String service = "quoter";
    private String uri = "quotes";
    private String event = "R";
}

