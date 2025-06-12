package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.ShippingMethod;

@Data
@Entity
@Table(name = "\"OrderShipping\"")
public class OrderShippingEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"order\"")
	private OrderEntity orderEntity;

	@Column(name = "\"street_name\"")
	private String streetName;

	@Column(name = "\"num_ext\"")
	private String numExt;

	@Column(name = "\"num_int\"")
	private String numInt;

	@Column(name = "\"zip_code\"")
	private String zipCode;

	@Column(name = "\"suburb\"")
	private String suburb;

	@Column(name = "\"municipality\"")
	private String municipality;

	@Column(name = "\"state\"")
	private String state;

	@Column(name = "\"city\"")
	private String city;

	@Column(name = "\"country\"")
	private String country;

	@Column(name = "\"references\"")
	private String references;

	@Column(name = "\"provider\"")
	private String provider;

	@Column(name = "\"service_code\"")
	private String serviceCode;

	@Column(name = "\"service_name\"")
	private String serviceName;

	@Column(name = "\"price\"")
	private BigDecimal price;

	@Column(name = "\"date\"")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Column(name = "\"shipping_method\"")
	@Enumerated(EnumType.STRING)
	private ShippingMethod shippingMethod;

	@Column(name = "\"rate_id\"")
	private String rateId;

	@Column(name = "\"tracking_number\"")
	private String trackingNumber;

	@Column(name = "\"tracking_url_provider\"")
	private String trackingUrlProvider;

}
