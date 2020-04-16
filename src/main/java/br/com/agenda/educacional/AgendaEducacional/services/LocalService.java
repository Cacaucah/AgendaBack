package br.com.agenda.educacional.AgendaEducacional.services;

import java.util.Optional;

import br.com.agenda.educacional.AgendaEducacional.model.entity.Localizacao;

public interface LocalService {

	Localizacao salvarLocal(Localizacao local);
	
	//atualiza o local
	Localizacao atualizarLocalizacao(Localizacao local);
	
	Optional<Localizacao> obterPorId(Long id);


}
