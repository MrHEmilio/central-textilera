package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx;

import lombok.Data;

@Data
public class Security {
    private String user;
    private String password;
    private int type = 1;
    private String token;
}
