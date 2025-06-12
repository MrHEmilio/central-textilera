package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
@Table(name = "\"ClothBilling\"")
public class ClothBillingEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"cloth\"")
	@JsonIgnore
	private ClothEntity clothEntity;

	@Column(name = "\"product_code\"")
	private String productCode;

	@Column(name = "\"product_label\"")
	private String productLabel;

	@Column(name = "\"unit_code\"")
	private String unitCode;

	@Column(name = "\"unit_label\"")
	private String unitLabel;

	public ClothBillingEntity(final ClothBillingEntity clothBillingEntity) {
		this.id = clothBillingEntity.id;
		this.clothEntity = null;
		this.productCode = clothBillingEntity.productCode;
		this.productLabel = clothBillingEntity.productLabel;
		this.unitCode = clothBillingEntity.unitCode;
		this.unitLabel = clothBillingEntity.unitLabel;
	}

}
