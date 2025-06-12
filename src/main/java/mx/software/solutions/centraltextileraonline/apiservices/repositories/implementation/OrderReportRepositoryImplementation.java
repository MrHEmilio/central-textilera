package mx.software.solutions.centraltextileraonline.apiservices.repositories.implementation;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Join;

import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.OrderEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.OrderStatusEntity;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.OrderReportRepository;

@Repository
@Slf4j
public class OrderReportRepositoryImplementation implements OrderReportRepository {

	@Autowired
	private EntityManager entityManager;

	@Override
	public BigDecimal sumTotal(final Date dateStart, final Date dateEnd) {

		final var criteriaBuilder = this.entityManager.getCriteriaBuilder();
		final var criteriaQuery = criteriaBuilder.createQuery(BigDecimal.class);
		final var rootOrderEntity = criteriaQuery.from(OrderEntity.class);
		final Join<OrderEntity, OrderStatusEntity> joinOrderStatus = rootOrderEntity.join("orderStatusEntities");

		criteriaQuery.select(criteriaBuilder.sum(rootOrderEntity.get("total")));
		if(dateStart != null && dateEnd !=null){
			final var predicateDateRange = criteriaBuilder.between(joinOrderStatus.get("date"), dateStart, dateEnd);
			final var predicateStatus = criteriaBuilder.equal(joinOrderStatus.get("status"), OrderStatus.REVISION);
			criteriaQuery.where(criteriaBuilder.and(predicateDateRange, predicateStatus));
		}

		return this.entityManager.createQuery(criteriaQuery).getSingleResult();
	}

}
