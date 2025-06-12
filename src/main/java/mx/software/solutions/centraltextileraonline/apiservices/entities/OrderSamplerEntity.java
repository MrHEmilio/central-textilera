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
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "\"OrderSampler\"")
public class OrderSamplerEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"cloth\"")
	private ClothEntity clothEntity;

	@Column(name = "\"sampler_name\"")
	private String samplerName;

	@Column(name = "\"amount\"")
	private BigDecimal amount;

	@Column(name = "\"sell_price\"")
	private BigDecimal sellPrice;

	@Column(name = "\"total_sell_price\"")
	private BigDecimal totalSellPrice;

	@Column(name = "\"discount\"")
	private BigDecimal discount;

}
