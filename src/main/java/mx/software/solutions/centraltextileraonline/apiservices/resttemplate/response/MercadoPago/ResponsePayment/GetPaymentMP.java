package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.MercadoPago.ResponsePayment;

import lombok.Data;

@Data
public class GetPaymentMP {
    private OrderMP order;
    private String status;
}
