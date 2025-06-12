package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "\"OrderBilling\"")
public class OrderBillingEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"order\"")
	private OrderEntity orderEntity;

	@Column(name = "\"billing_id\"")
	private String billingId;

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

	@Column(name = "\"rfc\"")
	private String rfc;

	@Column(name = "\"company_name\"")
	private String companyName;

	@Column(name = "\"fiscal_regimen\"")
	private String fiscalRegimen;

	@Column(name = "\"iva\"")
	private BigDecimal iva;

}
