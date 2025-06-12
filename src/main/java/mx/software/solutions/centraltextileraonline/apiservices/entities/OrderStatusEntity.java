package mx.software.solutions.centraltextileraonline.apiservices.entities;

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.OrderStatus;

@Data
@Entity
@Table(name = "\"OrderStatus\"")
public class OrderStatusEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"admin\"")
	private AdminEntity adminEntity;

	@Column(name = "\"status\"")
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Column(name = "\"date\"")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

}
