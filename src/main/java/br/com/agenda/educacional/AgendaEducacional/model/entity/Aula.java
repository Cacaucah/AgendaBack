/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.model.entity;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

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
@Table(name = "aulas", schema = "agendaeducacional")
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
	@JoinColumn(name = "id_materia")
	private Materia materia;
	
	@ManyToOne
	@JoinColumn(name = "id_aluno")
	private Aluno aluno;
	
	@ManyToOne
	@JoinColumn(name = "id_instituicao")
	private Instituicao instituicao;
	
	@ManyToOne
	@JoinColumn(name = "id_professor")
	private Professor professor;
	
	@Column(name = "tipo_de_aula")
	@Enumerated(value = EnumType.STRING)	
	private NaturezaAula tipoDeAula;
	
	@Column(name = "data")
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	private LocalDate data;
	
	@Column(name = "hora")
	private Time hora;
	
	@Column(name = "detalhes")
	private String detalhes;
	
	@Column(name  = "valor")
	private BigDecimal valor;
}
