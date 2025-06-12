package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.OrderCreateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ServicieSaveJsonMP {

    @Value("${path.fileJson}")
    private String dirPath;

    private ObjectMapper objMap;

    public void saveOrderMP(OrderCreateRequest orderCreateRequest){
        Map<String, OrderCreateRequest> orderPreference = new HashMap<>();

        orderPreference.put(orderCreateRequest.getPaymentId(), orderCreateRequest);

        //Escribir esta orden en el JSON


    }

}
