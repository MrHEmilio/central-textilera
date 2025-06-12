package mx.software.solutions.centraltextileraonline.apiservices.controllers;

import java.util.List;

import javax.validation.Valid;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.resttemplate.response.MercadoPago.Notification;
import mx.software.solutions.centraltextileraonline.apiservices.service.implementation.PaymentServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations.PaymentDocumentation;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CalculateInventoryProductRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CalculatePriceProductRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CheckoutMercadoPagoCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CalculatePriceResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CheckoutResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.CheckoutException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.InventoryException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.service.PaymentService;

@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController implements PaymentDocumentation {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private PaymentServiceImplementation checkMP;

	@Value("${token.mercado-pago.secretKey}")
	private String secretKey;


	@Override
	@PostMapping("/calculate/price/product")
	public List<CalculatePriceResponse> calculatePriceProduct(@Valid @RequestBody final CalculatePriceProductRequest calculatePriceProductRequest) {
		return this.paymentService.calculatePriceProduct(calculatePriceProductRequest.getCloths(), calculatePriceProductRequest.getSamplers());
	}

	@Override
	@PostMapping("/calculate/inventory/product")
	public boolean calculateInventoryProduct(@Valid @RequestBody final CalculateInventoryProductRequest calculateInventoryProductRequest) throws InventoryException {
		return this.paymentService.calculateInventoryProduct(calculateInventoryProductRequest.getCloths(), calculateInventoryProductRequest.getSamplers());
	}

	@Override
	@PostMapping("/checkout/mercado/pago")
	public CheckoutResponse createCheckoutMercadoPago(@Valid @RequestBody final CheckoutMercadoPagoCreateRequest checkoutMercadoPagoCreate) throws CheckoutException, DataBaseException, NotFoundException {
		return this.paymentService.createCheckoutMercadoPago(checkoutMercadoPagoCreate);
	}

	@CrossOrigin(origins = "https://api.mercadopago.com")
	@PostMapping("/notification")
	public HttpStatus notification(
			@RequestBody Notification notificaMP
			// @RequestHeader (value = "x-request-id") String idReq,
			// @RequestHeader (value = "x-signature") String signature
			) throws MPException, MPApiException, NotFoundException, DataBaseException {

		// System.out.println("DATA ID "+dataId);

		//PRODUCCIÓN
		PaymentController.log.info("starting notification MP ");

		/* PaymentController.log.info("Starting the separation of the request body in v1: && ts");

		// String v1Header = signature.split(",")[1].substring(3);
		// System.out.println("TokenKey: "+v1Header);
		// String tsHeader = signature.split(",")[0].substring(3);

		// System.out.printf("v1: %s ts: %s ",v1Header, tsHeader);

		//PaymentController.log.info("Finishing the separation of the requirement body in v1: && ts");

		// PaymentController.log.info("Starting the online && offline calculation of the requirement");

		// String signedTemplate = String.format("id:%s;request-id:%s;ts:%s", notificaMP.getData().getId(), idReq, tsHeader);

		// PaymentController.log.info("Finalizing the online && offline calculation of the requirement");

		// System.out.println("SignedTemplete: "+signedTemplate);

		// PaymentController.log.info("Starting the analysis of the signature, for the requirement");

		// String cyphedSignature = new HmacUtils("HmacSHA256", this.secretKey.toString()).hmacHex(signedTemplate);

		// System.out.println("Secret Key MP "+this.secretKey);

		// System.out.println("CYPHEDN "+cyphedSignature);

		// PaymentController.log.info("Finalizing the analysis of the signature, for the requirement");*/

		/*if (!v1Header.equals(cyphedSignature)){
			PaymentController.log.error("The signature is not authorized for the request");
			return HttpStatus.BAD_REQUEST;
		}*/
		PaymentController.log.info("Sending request for information processing");
		checkMP.checkMP(notificaMP);
		return HttpStatus.OK;
	}

}
