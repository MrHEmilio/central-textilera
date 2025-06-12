package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DeliveryMethod;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.PaymentMethod;

@Data
@Entity
@Table(name = "\"Order\"")
public class OrderEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"client\"")
	private ClientEntity clientEntity;

	@Column(name = "\"number\"")
	private int number;

	@Column(name = "\"total\"")
	private BigDecimal total;

	@Column(name = "\"delivery_method\"")
	@Enumerated(EnumType.STRING)
	private DeliveryMethod deliveryMethod;

	@Column(name = "\"payment_method\"")
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;

	@Column(name = "\"payment_id\"")
	private String paymentId;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "\"order\"", nullable = false)
	List<OrderClothEntity> orderClothEntities;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "\"order\"", nullable = false)
	List<OrderSamplerEntity> orderSamplerEntities;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "\"order\"", nullable = false)
	List<OrderStatusEntity> orderStatusEntities;

	@OneToOne(mappedBy = "orderEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private OrderShippingEntity orderShippingEntity;

	@OneToOne(mappedBy = "orderEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private OrderBillingEntity orderBillingEntity;

}
