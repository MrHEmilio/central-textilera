package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CatalogResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.PaginationResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DeliveryMethod;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.OrderStatus;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.PaymentMethod;
import mx.software.solutions.centraltextileraonline.apiservices.service.CatalogService;

@Service
public class CatalogServiceImplementation implements CatalogService {

	@Override
	public GetResponse<CatalogResponse<DeliveryMethod>> getAllDeliveryMethod() {
		final var listCatalogResponseDeliveryMethod = new ArrayList<CatalogResponse<DeliveryMethod>>();
		for (final DeliveryMethod deliveryMethod : DeliveryMethod.values())
			listCatalogResponseDeliveryMethod.add(new CatalogResponse<>(deliveryMethod, deliveryMethod.getLabel()));
		final var size = listCatalogResponseDeliveryMethod.size();
		final var paginationResponse = new PaginationResponse(1, size, 1, size);
		return new GetResponse<>(listCatalogResponseDeliveryMethod, paginationResponse);
	}

	@Override
	public GetResponse<CatalogResponse<PaymentMethod>> getAllPaymentMethod() {
		final var listCatalogResponsePaymentMethod = new ArrayList<CatalogResponse<PaymentMethod>>();
		for (final PaymentMethod paymentMethod : PaymentMethod.values())
			listCatalogResponsePaymentMethod.add(new CatalogResponse<>(paymentMethod, paymentMethod.getLabel()));
		final var size = listCatalogResponsePaymentMethod.size();
		final var paginationResponse = new PaginationResponse(1, size, 1, size);
		return new GetResponse<>(listCatalogResponsePaymentMethod, paginationResponse);
	}

	@Override
	public GetResponse<CatalogResponse<OrderStatus>> getAllOrderStatus() {
		final var listCatalogResponseOrderStatus = new ArrayList<CatalogResponse<OrderStatus>>();
		for (final OrderStatus orderStatus : OrderStatus.values())
			listCatalogResponseOrderStatus.add(new CatalogResponse<>(orderStatus, orderStatus.getLabel()));
		final var size = listCatalogResponseOrderStatus.size();
		final var paginationResponse = new PaginationResponse(1, size, 1, size);
		return new GetResponse<>(listCatalogResponseOrderStatus, paginationResponse);
	}

}
