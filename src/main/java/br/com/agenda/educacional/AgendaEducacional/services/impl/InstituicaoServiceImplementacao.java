/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.services.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import br.com.agenda.educacional.AgendaEducacional.exceptions.RegraNegocioException;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Instituicao;
import br.com.agenda.educacional.AgendaEducacional.model.repository.InstituicaoRepository;
import br.com.agenda.educacional.AgendaEducacional.services.InstituicaoService;

/**
 * @author Ana Caroline
 *
 */
@Service
public class InstituicaoServiceImplementacao implements InstituicaoService{

	private final InstituicaoRepository repository;

	public InstituicaoServiceImplementacao(InstituicaoRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public Instituicao salvarInstituicao(Instituicao instituicao) {
		validar(instituicao);
		return repository.save(instituicao);
	}

	public void validar(Instituicao instituicao) {
		if(instituicao.getId()==null && instituicao.getNome().trim().equals("")){
			throw new RegraNegocioException("Descrição inválida.");
		}
		if(instituicao.getRua().trim().equals("")){
			throw new RegraNegocioException("Informe uma rua.");
		}
		if(Long.toString(instituicao.getNumero()).equals("")){
			throw new RegraNegocioException("Informe um numero.");
		}
		
	}

	@Override
	public Instituicao atualizarInstituicao(Instituicao instituicao) {
		//verifica se o objeto que chega não é nullo
		Objects.requireNonNull(instituicao.getId());
		//se não for ele passa novamente pela validação
		validar(instituicao);
		// se for valido, então salva a instituicao
		return repository.save(instituicao);
	}

	@Override
	public void deletar(Instituicao instituicao) {
		// TODO Auto-generated method stub
		Objects.requireNonNull(instituicao.getId());
		repository.delete(instituicao);
	}

	@Override
	public Optional<Instituicao> obterPorId(Long id) {
		return repository.findById(id);
	}

	@Override
	public List<Instituicao> buscar(Instituicao instuicoesFiltro) {
		Example example = Example.of(instuicoesFiltro,
				ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

		return repository.findAll(example);
	}

	@Override
	public Instituicao getById(Long idInstituicao) {
		return repository.getById(idInstituicao);
	}

}
