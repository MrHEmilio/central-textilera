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
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;

@Data
@Entity
@Table(name = "\"UseHistory\"")
public class UseHistoryEntity {

	@Id
	@GeneratedValue
	@Column(name = "\"id\"")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"use\"")
	private UseEntity useEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "\"admin\"")
	private AdminEntity adminEntity;

	@Column(name = "\"action_type\"")
	@Enumerated(EnumType.STRING)
	private DataBaseActionType actionType;

	@Column(name = "\"date\"")
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Column(name = "\"object\"")
	private String object;

}
