package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.pqtxResponse;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class QuotationResponse {
    private String serviceType;
    private String id;
    private String idRef;
    private String serviceName;
    private String serviceInfoDescr;
    private String serviceInfoDescrLong;
    private String cutoffDateTime;
    private String cutoffTime;
    private String maxRadTime;
    private String maxBokTime;
    private String onTime;
    private String promiseDate;
    private int promiseDateDaysQty;
    private int promiseDateHoursQty;
    private boolean inOffer;
    private Object shipmentDetail;
    private Object services;
    private AmountResponse amount;
}
