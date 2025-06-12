package mx.software.solutions.centraltextileraonline.apiservices.service;

import java.util.UUID;

import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.DateFilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.OrderCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.OrderFilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.OrderUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.OrderResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.OrderStatusException;

public interface OrderService {

	OrderResponse createOrder(OrderCreateRequest orderCreateRequest, UUID client) throws DataBaseException, NotFoundException;

	OrderResponse updateOrder(OrderUpdateRequest orderUpdateRequest, UUID admin) throws DataBaseException, NotFoundException, OrderStatusException;

	GetResponse<OrderResponse> getAllOrder(OrderFilterRequest orderFilterRequest, DateFilterRequest dateFilter, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	GetResponse<OrderResponse> getAllOrderByClient(UUID client, OrderFilterRequest orderFilterRequest, DateFilterRequest dateFilter, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	OrderResponse getOrderById(UUID order) throws DataBaseException, NotFoundException;

}
