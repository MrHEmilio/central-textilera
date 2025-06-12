package mx.software.solutions.centraltextileraonline.apiservices.repositories;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.OrderEntity;

public interface OrderRepository extends PagingAndSortingRepository<OrderEntity, UUID> {

	@Query(value = "SELECT * FROM get_order(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9) ORDER BY number DESC", countQuery = "SELECT count(*) FROM get_order(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9)", nativeQuery = true)
	Page<OrderEntity> findAll(UUID client, Integer number, String clientName, String orderStatus, String deliveryMethod, String paymentMethod, Date dateStart, Date dateEnd, String direction, Pageable pageable);

	Optional<OrderEntity> findFirstByOrderByNumberDesc();

	Optional<OrderEntity> findByPaymentId(String paymentId);

}
