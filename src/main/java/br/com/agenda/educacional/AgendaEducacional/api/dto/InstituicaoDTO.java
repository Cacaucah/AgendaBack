/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.api.dto;

import br.com.agenda.educacional.AgendaEducacional.enums.Situacao;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Localizacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Ana Caroline
 *
 */
@Setter
@Getter
@NoArgsConstructor
//cria um construtor com argumentos
@AllArgsConstructor
public class InstituicaoDTO {
	private String nome;
	private String rua;
	private Long numero;
	private String cep;
	private Long professor;
	private String estado;
	private String cidade;
	private Situacao situacao;
	private String telefone;
}
