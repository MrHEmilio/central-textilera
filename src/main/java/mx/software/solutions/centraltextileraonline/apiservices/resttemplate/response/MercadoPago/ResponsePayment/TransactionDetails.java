package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.MercadoPago.ResponsePayment;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDetails {
    private BigDecimal net_received_amount;
    private BigDecimal total_paid_amount;
    private BigDecimal overpaid_amount;
    private BigDecimal installment_amount;

}
