package mx.software.solutions.centraltextileraonline.apiservices.repositories.implementation;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;

import mx.software.solutions.centraltextileraonline.apiservices.entities.CountryCodeEntity;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.CountryCodeSearchRepository;

@Repository
public class CountryCodeSearchRepositoryImplementation implements CountryCodeSearchRepository {

	@Autowired
	private EntityManager entityManager;

	@Override
	public Page<CountryCodeEntity> findAll(final String search, final Pageable pageable) {
		final var listCountryCodeEntities = this.getListCountryCodeEntities(search, pageable);
		final var totalRecords = this.getTotalRecords(search);
		return new PageImpl<>(listCountryCodeEntities, pageable, totalRecords);
	}

	private Predicate[] getPredicates(final CriteriaBuilder criteriaBuilder, final Root<CountryCodeEntity> rootCountryCodeEntity, final String search) {
		final List<Predicate> listPredicates = new ArrayList<>();

		if (search != null && !search.isBlank()) {
			final var functionUnaccent = criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(rootCountryCodeEntity.get("name")));
			final var searchFormat = StringUtils.lowerCase(StringUtils.stripAccents(search));
			listPredicates.add(criteriaBuilder.like(functionUnaccent, "%" + searchFormat + "%"));
		}

		return listPredicates.toArray(new Predicate[0]);
	}

	private List<CountryCodeEntity> getListCountryCodeEntities(final String search, final Pageable pageable) {
		final var criteriaBuilder = this.entityManager.getCriteriaBuilder();
		final var criteriaQuery = criteriaBuilder.createQuery(CountryCodeEntity.class);
		final var rootCountryCodeEntity = criteriaQuery.from(CountryCodeEntity.class);
		final var predicates = this.getPredicates(criteriaBuilder, rootCountryCodeEntity, search);
		criteriaQuery.where(predicates);
		if (pageable != null)
			criteriaQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), rootCountryCodeEntity, criteriaBuilder));

		final var typedQuery = this.entityManager.createQuery(criteriaQuery);
		if (pageable != null) {
			typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
			typedQuery.setMaxResults(pageable.getPageSize());
		}

		return typedQuery.getResultList();
	}

	private long getTotalRecords(final String search) {
		final var criteriaBuilder = this.entityManager.getCriteriaBuilder();
		final var criteriaQuery = criteriaBuilder.createQuery(Long.class);
		final var rootCollectionEntity = criteriaQuery.from(CountryCodeEntity.class);
		final var predicates = this.getPredicates(criteriaBuilder, rootCollectionEntity, search);
		criteriaQuery.select(criteriaBuilder.count(rootCollectionEntity));
		criteriaQuery.where(predicates);
		return this.entityManager.createQuery(criteriaQuery).getSingleResult();
	}

}
