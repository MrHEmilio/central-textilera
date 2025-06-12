package mx.software.solutions.centraltextileraonline.apiservices.controllers.documentations;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.DateFilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.OrderCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.OrderCreateWithoutAccountRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.OrderFilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.OrderUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.CrudResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.OrderResponse;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.OrderStatusException;

@Tag(name = "Order", description = "Endpoints to manage the Order realized in Central Textilera Ecommerce.")
public interface OrderDocumentation {

	@Operation(summary = "Create Order", description = "Create order in total cart of Central Textilera Ecommerce.")
	CrudResponse<OrderResponse> createOrder(OrderCreateRequest orderCreateRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Create Order Without Account", description = "Create order in total cart of Central Textilera Ecommerce.")
	CrudResponse<OrderResponse> createOrderWithoutAccount(OrderCreateWithoutAccountRequest orderCreateWithoutAccountRequest) throws DataBaseException, NotFoundException, ExistException;

	@Operation(summary = "Update Order", description = "Update order status of Central Textilera Ecommerce.")
	CrudResponse<OrderResponse> updateOrder(OrderUpdateRequest orderUpdateRequest) throws DataBaseException, NotFoundException, OrderStatusException;

	@Operation(summary = "Get all Order", description = "Get all order of Central Textilera Ecommerce.")
	GetResponse<OrderResponse> getAllOrder(OrderFilterRequest orderFilterRequest, DateFilterRequest dateFilter, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get all Order by client", description = "Get all order by client of Central Textilera Ecommerce.")
	GetResponse<OrderResponse> getAllOrderByClient(UUID client, OrderFilterRequest orderFilterRequest, DateFilterRequest dateFilter, PaginationRequest paginationRequest) throws DataBaseException, NotFoundException;

	@Operation(summary = "Get Order by id", description = "Get order by id for tracking of Central Textilera Ecommerce.")
	OrderResponse getOrderById(UUID order) throws DataBaseException, NotFoundException;

}
