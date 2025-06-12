package mx.software.solutions.centraltextileraonline.apiservices.helpers;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaymentClothVariantRequest;
import mx.software.solutions.centraltextileraonline.apiservices.dtos.MeasureDto;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClothVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
public class WeightHelper {

    @Autowired
    private ShippingHelper shippingHelper;

    public String checkWeigth(final List<PaymentClothVariantRequest> listPaymentClothVariantRequest) {
        String response;

        BigDecimal totalWeight = this.shippingHelper
                .getMeasureDto(listPaymentClothVariantRequest)
                .stream()
                .map(MeasureDto::getWeight)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalWeight.compareTo(new BigDecimal(30)) <= 0)  // SKYDROP
            response = "SKY";
         else
            response = "PQTX";

        WeightHelper.log.info("Finish Calculate Weight Shipping Helper");
        return response;
    }
}
