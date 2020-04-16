package br.com.agenda.educacional.AgendaEducacional.api.dto;

import br.com.agenda.educacional.AgendaEducacional.enums.Situacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//cria um construtor com argumentos
@AllArgsConstructor
public class AlunoDTO {
	private String nome;
	private Situacao situacao;
	private Long professor;
}
