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
@Table(name = "\"ClothSamplerBilling\"")
public class ClothSamplerBillingEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"cloth_sampler\"")
	@JsonIgnore
	private ClothSamplerEntity clothSamplerEntity;

	@Column(name = "\"product_code\"")
	private String productCode;

	@Column(name = "\"product_label\"")
	private String productLabel;

	@Column(name = "\"unit_code\"")
	private String unitCode;

	@Column(name = "\"unit_label\"")
	private String unitLabel;

	public ClothSamplerBillingEntity(final ClothSamplerBillingEntity clothSamplerBillingEntity) {
		this.id = clothSamplerBillingEntity.id;
		this.clothSamplerEntity = null;
		this.productCode = clothSamplerBillingEntity.productCode;
		this.productLabel = clothSamplerBillingEntity.productLabel;
		this.unitCode = clothSamplerBillingEntity.unitCode;
		this.unitLabel = clothSamplerBillingEntity.unitLabel;
	}

}
