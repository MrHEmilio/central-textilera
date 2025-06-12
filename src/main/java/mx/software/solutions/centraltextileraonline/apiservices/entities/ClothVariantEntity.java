package mx.software.solutions.centraltextileraonline.apiservices.entities;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "\"ClothVariant\"")
public class ClothVariantEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"cloth\"", insertable = false, updatable = false)
	@JsonIgnore
	private ClothEntity clothEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"color\"")
	private ColorEntity colorEntity;

	@Column(name = "\"amount\"")
	private BigDecimal amount;

	public ClothVariantEntity(final ClothVariantEntity clothVariantEntity) {
		this.id = clothVariantEntity.id;
		this.clothEntity = null;
		this.colorEntity = new ColorEntity(clothVariantEntity.colorEntity);
		this.amount = clothVariantEntity.amount;
	}

}
