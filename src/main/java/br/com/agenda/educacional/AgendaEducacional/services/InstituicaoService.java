package br.com.agenda.educacional.AgendaEducacional.services;

import java.util.Optional;

import br.com.agenda.educacional.AgendaEducacional.model.entity.Instituicao;

public interface InstituicaoService {

	Instituicao salvarInstituicao(Instituicao instituicao);
	
	Instituicao atualizarInstituicao(Instituicao instituicao);
	
	void deletar(Instituicao instituicao);
	
    Optional<Instituicao> obterPorId(Long id);

}
