/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.agenda.educacional.AgendaEducacional.enums.NaturezaAula;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ana Caroline
 *
 */
@Entity
@Table(name = "aula", schema = "agendaeducacional")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aula {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	//dizendo ao java que o valor é um Long
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "materia")
	private Materia materia;
	
	@ManyToOne
	@JoinColumn(name = "aluno")
	private Aluno aluno;
	
	@ManyToOne
	@JoinColumn(name = "instituicao")
	private Instituicao instituicao;
	
	@ManyToOne
	@JoinColumn(name = "professor")
	private Professor professor;
	
	@Column(name = "aula")
	@Enumerated(value = EnumType.STRING)	
	private NaturezaAula tipoDeAula;
	
	@Column(name = "data")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	@JsonProperty("data")
	private LocalDate data;
	
	@Column(name = "hora_inicial")
	//formata o padrão de retorno de hora que desejo
    @JsonFormat(pattern = "HH:mm")
	private LocalTime horaInicial;
	

	@Column(name = "hora_fim")
	//formata o padrão de retorno de hora que desejo
	@JsonFormat(pattern = "HH:mm")
	private LocalTime horaFim;
	
	@Column(name = "detalhes")
	private String detalhes;
	
	@Column(name  = "valor")
	private BigDecimal valor;
}
