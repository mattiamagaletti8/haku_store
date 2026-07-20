package com.betacom.jpa.models;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Proprietario: Pier
@Setter
@Getter
@ToString
@Entity
@Table(name = "carrello")
public class Carrello {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_carrello")
	private Integer idCarrello;

	@OneToOne
	@JoinColumn(
			name = "id_utente",
			nullable = false,
			unique = true,
			foreignKey = @ForeignKey(name = "fk_carrello_utente")
			)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Utente utente;

	@ManyToOne
	@JoinColumn(
			name = "id_coupon",
			foreignKey = @ForeignKey(name = "fk_carrello_coupon")
			)
	private Coupon coupon;

	@Column(name = "data_creazione", nullable = false)
	private LocalDateTime dataCreazione;

	@OneToMany(mappedBy = "carrello", fetch = FetchType.EAGER)
	private List<DettaglioCarrello> righe;

}
