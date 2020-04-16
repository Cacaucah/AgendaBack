/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class ProfessorDTO {
	//as anotações acima já criam os métodos setter, getter e um construtor para os atributos abaixo
    private String nome;
	private String email;
    private String senha;
}
