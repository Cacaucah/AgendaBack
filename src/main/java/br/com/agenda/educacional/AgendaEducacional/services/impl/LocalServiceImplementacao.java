/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.services.impl;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.agenda.educacional.AgendaEducacional.exceptions.RegraNegocioException;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Localizacao;
import br.com.agenda.educacional.AgendaEducacional.model.repository.LocalizacaoRepository;
import br.com.agenda.educacional.AgendaEducacional.services.LocalService;

/**
 * @author Ana Caroline
 *
 */
@Service
public class LocalServiceImplementacao implements LocalService {

	private final LocalizacaoRepository repository;

	public LocalServiceImplementacao(LocalizacaoRepository repository) {
		this.repository = repository;
	}
	
	
	@Override
	public Localizacao salvarLocal(Localizacao local) {
		validar(local);
		return repository.save(local);
	}

	@Override
	public Localizacao atualizarLocalizacao(Localizacao local) {
			//verifica se o objeto que chega não é nullo
			Objects.requireNonNull(local.getId());
			//se não for ele passa novamente pela validação
			validar(local);
			// se for valido, então salva
			return repository.save(local);
	}
	private void validar(Localizacao local) {
		if(local.getId()==null || local.getRua().trim().equals("")){
			throw new RegraNegocioException("Descrição inválida.");
		}
		
	}

	@Override
	public Optional<Localizacao> obterPorId(Long id) {
		return repository.findById(id);
	}

}
