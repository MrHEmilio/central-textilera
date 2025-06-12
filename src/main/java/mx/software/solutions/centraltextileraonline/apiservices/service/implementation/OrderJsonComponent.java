package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.OrderCreateRequest;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.io.File;
import java.util.*;
import lombok.Data;

@Slf4j
@Component
public class OrderJsonComponent {
    @Value("${path.fileJson}")
    private String pathJson;

    private HelperJson helperJson = null;
    private ObjectMapper objMap = new ObjectMapper();

    List<HelperJson> listOrdersTemps;

    public void receiveOrder(OrderCreateRequest orderCreateRequest, UUID idUser) {
        OrderJsonComponent.log.info("Starting temporary order storage {} ", orderCreateRequest.getPaymentId());
        helperJson = new HelperJson(orderCreateRequest.getPaymentId(), orderCreateRequest, idUser);
        try {
            saveInJson(helperJson);
        } catch (Exception e) {
            OrderJsonComponent.log.error("An error occurred while temporarily storing the order {} error {} ", orderCreateRequest.getPaymentId(), e.getMessage());
        }
        OrderJsonComponent.log.info("Temporarily ending order storage {} ", orderCreateRequest.getPaymentId());
    }

    public void saveInJson(HelperJson orderHelper) {
        OrderJsonComponent.log.info("Starting to write the command to the temporary storage file");
        File fileJson = new File(this.pathJson);
        if (orderHelper != null) {
            if (fileJson.exists()) {
                try {
                    if (fileJson.length() < 3){
                        listOrdersTemps = new ArrayList();
                    } else {
                        listOrdersTemps = new ArrayList<>(Arrays.asList(objMap.readValue(fileJson, HelperJson[].class)));
                        if (listOrdersTemps.stream().anyMatch(item -> item.getPayId().equals(orderHelper.getPayId()))) {
                            OrderJsonComponent.log.warn("The record already exists, it will be skipped being added to temporary storage {} ", orderHelper.getPayId());
                            return;
                        }
                    }

                } catch (IOException ioE) {
                    OrderJsonComponent.log.info("Error reading existing orders: {} ",ioE.toString());
                }

                listOrdersTemps.add(orderHelper);

                try {
                    objMap.writeValue(fileJson, listOrdersTemps);
                    OrderJsonComponent.log.info("Preference {} successfully written to JSON file", orderHelper.getPayId());
                } catch (IOException ioE) {
                    OrderJsonComponent.log.error("An error occurred while writing to the JSON file: {} ", orderHelper.getPayId());
                }
            } else {
                OrderJsonComponent.log.info("An error occurred while writing to the Order in the JSON file");
            }
        }
        OrderJsonComponent.log.info("Finishing writing command to temporary storage file {} ", orderHelper.getPayId());
    }

    public HelperJson searchPreference(String idPreference) {
        File fileJson = new File(this.pathJson);
        Optional<HelperJson> orderPreference = null;
        OrderJsonComponent.log.info("Inciting the PREFERENCEID search in the JSON file {} ", idPreference);
        if (fileJson.exists()) {
            try {
                if (fileJson.length() > 0) {
                    listOrdersTemps = new ArrayList<>(Arrays.asList(objMap.readValue(fileJson, HelperJson[].class)));
                    orderPreference = listOrdersTemps.stream()
                            .filter(item -> item.getPayId().equals(idPreference))
                            .findFirst();
                } else {
                    OrderJsonComponent.log.error("There are NO records in the temporary file, nothing to show. {} ", fileJson.getName());
                }
            } catch (IOException ioE) {
                OrderJsonComponent.log.info("Error reading existing file: {} ", ioE.getMessage());
            }

        } else {
            OrderJsonComponent.log.info("File JSON not found");
        }
        return orderPreference.orElse(null);
    }

    public void delOrderJson(HelperJson orderPreference) {
        File fileJson = new File(this.pathJson);
        if (fileJson.exists() && fileJson.length() > 0) {
            try {
                listOrdersTemps = new ArrayList<>(Arrays.asList(objMap.readValue(fileJson, HelperJson[].class)));
                if(listOrdersTemps.removeIf(item -> item.getPayId().equals(orderPreference.getPayId())))
                    objMap.writeValue(fileJson, listOrdersTemps);

            } catch (IOException ioE) {
                OrderJsonComponent.log.info("Error reading existing JSONfile: {} ", ioE.getMessage());
            }
        } else
            OrderJsonComponent.log.info("The JSON file is not sent or has no content");
    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class HelperJson {
    private String payId;
    private OrderCreateRequest orderCreateRequest;
    private UUID idUser;
}
