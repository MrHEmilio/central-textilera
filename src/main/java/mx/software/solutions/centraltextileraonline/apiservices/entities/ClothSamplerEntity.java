package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.CascadeType;
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
@Table(name = "\"ClothSampler\"")
public class ClothSamplerEntity {

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

	@OneToOne(mappedBy = "clothSamplerEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private ClothSamplerBillingEntity clothSamplerBillingEntity;

	@Column(name = "\"description\"")
	private String description;

	@Column(name = "\"price\"")
	private BigDecimal price;

	@Column(name = "\"amount\"")
	private BigDecimal amount;

	public ClothSamplerEntity(final ClothSamplerEntity clothSamplerEntity) {
		this.id = clothSamplerEntity.id;
		this.clothEntity = null;
		this.description = clothSamplerEntity.description;
		this.price = clothSamplerEntity.price;
		this.amount = clothSamplerEntity.amount;
	}

}
