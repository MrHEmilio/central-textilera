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

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
@Table(name = "\"ClothMeasure\"")
public class ClothMeasureEntity {

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

	@Column(name = "\"dimension\"")
	private BigDecimal dimension;

	@Column(name = "\"average_per_roll\"")
	private BigDecimal averagePerRoll;

	@Column(name = "\"width\"")
	private BigDecimal width;

	@Column(name = "\"weight\"")
	private BigDecimal weight;

	@Column(name = "\"yield_per_kilo\"")
	private BigDecimal yieldPerKilo;

	public ClothMeasureEntity(final ClothMeasureEntity clothMeasureEntity) {
		this.id = clothMeasureEntity.id;
		this.clothEntity = null;
		this.dimension = clothMeasureEntity.dimension;
		this.width = clothMeasureEntity.width;
		this.weight = clothMeasureEntity.weight;
		this.yieldPerKilo = clothMeasureEntity.yieldPerKilo;
	}

}
