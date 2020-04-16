package br.com.agenda.educacional.AgendaEducacional.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.agenda.educacional.AgendaEducacional.enums.Situacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "aluno", schema = "agendaeducacional")
@Builder
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Aluno {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	// dizendo ao java que o valor é um Long
	private Long id;
	

	@Column(name = "nome")
	private String nome;

	@Column(name = "situacao")
	@Enumerated(value = EnumType.STRING)
	private Situacao situacao;

	@ManyToOne
	@JoinColumn(name = "professor")
	private Professor professor;

}
