package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.MercadoPago.ResponsePayment;

import lombok.Data;

@Data
public class NotificationPayer {
    private long id;
    private String email;
    private PayerIdentification identification;
    private String type;
}
