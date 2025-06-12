package mx.software.solutions.centraltextileraonline.apiservices.service;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CatalogResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DeliveryMethod;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.OrderStatus;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.PaymentMethod;

public interface CatalogService {

	GetResponse<CatalogResponse<DeliveryMethod>> getAllDeliveryMethod();

	GetResponse<CatalogResponse<PaymentMethod>> getAllPaymentMethod();

	GetResponse<CatalogResponse<OrderStatus>> getAllOrderStatus();

}
