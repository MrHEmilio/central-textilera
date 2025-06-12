package mx.software.solutions.centraltextileraonline.apiservices.repositories.implementation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import mx.software.solutions.centraltextileraonline.apiservices.enumerations.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ClothVariantEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ColorEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.OrderClothEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.OrderEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.OrderStatusEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ReportClothMostSoldEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.ReportClothSoldOutEntity;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.ClothReportRepository;

@Repository
public class ClothReportRepositoryImplementation implements ClothReportRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public long countByActive() {
        final var criteriaBuilder = this.entityManager.getCriteriaBuilder();
        final var criteriaQuery = criteriaBuilder.createQuery(Long.class);
        final var rootClothEntity = criteriaQuery.from(ClothEntity.class);
        final List<Predicate> listPredicates = new ArrayList<>();
        listPredicates.add(criteriaBuilder.isTrue(rootClothEntity.get("active")));
        final var predicates = listPredicates.toArray(new Predicate[0]);
        criteriaQuery.select(criteriaBuilder.count(rootClothEntity));
        criteriaQuery.where(predicates);
        return this.entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public Page<ReportClothMostSoldEntity> findByMostSold(final Date dateStart, final Date dateEnd, final Pageable pageable) {
        final var listClothEntities = this.getMostSoldResult(dateStart, dateEnd, pageable);
        final var totalRecords = this.getMostSoldCount(dateStart, dateEnd);
        return new PageImpl<>(listClothEntities, pageable, totalRecords);
    }

    private List<ReportClothMostSoldEntity> getMostSoldResult(final Date dateStart, final Date dateEnd, final Pageable pageable) {
        final var criteriaBuilder = this.entityManager.getCriteriaBuilder();
        final var criteriaQuery = criteriaBuilder.createQuery(ReportClothMostSoldEntity.class);
        final var rootOrderEntity = criteriaQuery.from(OrderEntity.class);

        final Join<OrderEntity, OrderClothEntity> joinOrderCloth = rootOrderEntity.join("orderClothEntities");
        final Join<OrderEntity, OrderStatusEntity> joinOrderStatus = rootOrderEntity.join("orderStatusEntities");
        final Join<OrderClothEntity, ClothEntity> joinCloth = joinOrderCloth.join("clothEntity");

        criteriaQuery.multiselect(joinCloth, criteriaBuilder.sum(joinOrderCloth.get("amount")));

        if (dateStart != null && dateEnd != null) {
            final var predicateDateRange = criteriaBuilder.between(joinOrderStatus.get("date"), dateStart, dateEnd);
            final var predicateStatus = criteriaBuilder.equal(joinOrderStatus.get("status"), OrderStatus.REVISION);
            criteriaQuery.where(criteriaBuilder.and(predicateDateRange, predicateStatus));
        }

        criteriaQuery.groupBy(joinCloth.get("id"));
        criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.literal(2)));

        final var typedQuery = this.entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
        return typedQuery.getResultList();
    }

    private long getMostSoldCount(final Date dateStart, final Date dateEnd) {
        final var criteriaBuilder = this.entityManager.getCriteriaBuilder();
        final var criteriaQuery = criteriaBuilder.createQuery(Long.class);
        final var rootOrderEntity = criteriaQuery.from(OrderEntity.class);

        final Join<OrderEntity, OrderClothEntity> joinOrderCloth = rootOrderEntity.join("orderClothEntities");
        final Join<OrderEntity, OrderStatusEntity> joinOrderStatus = rootOrderEntity.join("orderStatusEntities");
        final Join<OrderClothEntity, ClothEntity> joinCloth = joinOrderCloth.join("clothEntity");
        criteriaQuery.select(criteriaBuilder.countDistinct(joinCloth));

        if (dateStart != null && dateEnd != null) {
            /*final var predicateDateStart = criteriaBuilder.greaterThanOrEqualTo(joinOrderStatus.get("date"), dateStart);
            final var predicateDateEnd = criteriaBuilder.lessThan(joinOrderStatus.get("date"), dateEnd);
            final var predicateStatus = criteriaBuilder.equal(joinOrderStatus.get("status"), OrderStatus.REVISION);
            criteriaQuery.where(predicateDateStart, predicateDateEnd);*/

            final var predicateDateRange = criteriaBuilder.between(joinOrderStatus.get("date"), dateStart, dateEnd);
            final var predicateStatus = criteriaBuilder.equal(joinOrderStatus.get("status"), OrderStatus.REVISION);
            criteriaQuery.where(criteriaBuilder.and(predicateDateRange, predicateStatus));

        }

        return this.entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public Page<ReportClothSoldOutEntity> findBySoldOut(final Pageable pageable) {
        final var listClothEntities = this.getSoldOutResult(pageable);
        final var totalRecords = this.getSoldOutCount();
        return new PageImpl<>(listClothEntities, pageable, totalRecords);
    }

    private List<ReportClothSoldOutEntity> getSoldOutResult(final Pageable pageable) {
        final var criteriaBuilder = this.entityManager.getCriteriaBuilder();
        final var criteriaQuery = criteriaBuilder.createQuery(ReportClothSoldOutEntity.class);
        final var rootClothVariantEntity = criteriaQuery.from(ClothVariantEntity.class);

        final Join<ClothVariantEntity, ClothEntity> joinCloth = rootClothVariantEntity.join("clothEntity");
        final Join<ClothVariantEntity, ColorEntity> joinColor = rootClothVariantEntity.join("colorEntity");

        final var predicateAmount = criteriaBuilder.equal(rootClothVariantEntity.get("amount"), 0);

        criteriaQuery.multiselect(joinCloth, joinColor);
        criteriaQuery.where(predicateAmount);

        final var typedQuery = this.entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());

        return typedQuery.getResultList();
    }

    private long getSoldOutCount() {
        final var criteriaBuilder = this.entityManager.getCriteriaBuilder();
        final var criteriaQuery = criteriaBuilder.createQuery(Long.class);
        final var rootClothVariantEntity = criteriaQuery.from(ClothVariantEntity.class);
        final var predicateAmount = criteriaBuilder.equal(rootClothVariantEntity.get("amount"), 0);
        criteriaQuery.select(criteriaBuilder.count(rootClothVariantEntity));
        criteriaQuery.where(predicateAmount);
        return this.entityManager.createQuery(criteriaQuery).getSingleResult();
    }

}
