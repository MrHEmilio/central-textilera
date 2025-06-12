package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.MercadoPago;

import lombok.Data;

@Data
public class Notification {
    private String action;
    private String api_version;
    private DataNotification data;
    private String date_created;
    private String id;
    private boolean live_mode;
    private String type;
    private long user_id;
}
