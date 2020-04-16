/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.api.dto;

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
public class MateriaDTO {
    private Long id;
	private String nome;
	private Long professor;
}
