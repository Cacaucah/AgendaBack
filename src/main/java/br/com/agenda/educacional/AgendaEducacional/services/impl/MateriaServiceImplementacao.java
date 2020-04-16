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
import br.com.agenda.educacional.AgendaEducacional.model.entity.Localizacao;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Materia;
import br.com.agenda.educacional.AgendaEducacional.model.repository.LocalizacaoRepository;
import br.com.agenda.educacional.AgendaEducacional.model.repository.MateriaRepository;
import br.com.agenda.educacional.AgendaEducacional.services.MateriaService;

/**
 * @author Ana Caroline
 *
 */
@Service
public class MateriaServiceImplementacao implements MateriaService{
	
	private final MateriaRepository repository;

	public MateriaServiceImplementacao(MateriaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Materia salvarMateria(Materia materia) {
		validar(materia);
		return repository.save(materia);
	}

	@Override
	public Materia atualizarMateria(Materia materia) {
		//verifica se o objeto que chega não é nullo
		Objects.requireNonNull(materia.getId());
		//se não for ele passa novamente pela validação
		validar(materia);
		// se for valido, então salva
		return repository.save(materia);
	}
	public void validar(Materia materia) {
		if(materia.getId()==null && materia.getNome().trim().equals("")){
			throw new RegraNegocioException("Descrição inválida.");
		}
		if(materia.getNome().trim().equals("")){
			throw new RegraNegocioException("Informe o nome da materia.");
		}
	}
	@Override
	public void deletarMateria(Materia materia) {
		// verifica se o objeto que chega não é nullo
			Objects.requireNonNull(materia.getId());
			repository.delete(materia);
	}

	@Override
	public Optional<Materia> obterPorId(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

	@Override
	public List<Materia> buscarMaterias(Materia materiaFiltro) {
		Example example = Example.of(materiaFiltro,
				ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

		return repository.findAll(example);
	}

	@Override
	public Materia getById(Long idMateria) {
		return repository.getById(idMateria);
	}

}
