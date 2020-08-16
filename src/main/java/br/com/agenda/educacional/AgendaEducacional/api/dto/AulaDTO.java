/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.agenda.educacional.AgendaEducacional.enums.NaturezaAula;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ana Caroline
 *
 */
@Getter
@Setter
@NoArgsConstructor
//cria um construtor com argumentos
@AllArgsConstructor
public class AulaDTO {
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate data;
	private LocalTime horaInicial;
	private LocalTime horaFim;
	private String detalhes;
	private BigDecimal valor;
	private Long materia;
	private Long professor;
	private Long cliente;

}
