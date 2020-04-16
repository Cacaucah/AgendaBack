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
import org.springframework.transaction.annotation.Transactional;

import br.com.agenda.educacional.AgendaEducacional.exceptions.RegraNegocioException;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Aluno;
import br.com.agenda.educacional.AgendaEducacional.model.repository.AlunoRepository;
import br.com.agenda.educacional.AgendaEducacional.services.AlunoService;

/**
 * @author Ana Caroline
 *
 */
@Service
public class AlunoServiceImplementacao implements AlunoService {

	private final AlunoRepository repository;
	
	public AlunoServiceImplementacao(AlunoRepository repository){
		this.repository = repository;
	}
	
	@Override
	@Transactional
	public Aluno salvarAluno(Aluno aluno) {
		// TODO Auto-generated method stub
		validar(aluno);
		return repository.save(aluno);
	}

	@Override
	public Aluno atualizarAluno(Aluno aluno) {
		//para atualizar um aluno é obrigatório que o id passado não seja nullo
		Objects.requireNonNull(aluno.getId());
		
		validar(aluno);
		return repository.save(aluno);
	}
	@Override
	public void validar(Aluno aluno) {
		//valida se o campo aluno foi preenchido
		//se aluno for null ou o nome não for informado, lança exception
		if(aluno.getId()==null && aluno.getNome().trim().equals("")){
			throw new RegraNegocioException("Informe os dados corretamente!");
		}
		if(aluno.getId()!=null && aluno.getNome().trim().equals("")){
			throw new RegraNegocioException("Informe o nome do aluno.");
		}
	}

	@Override
	public Optional<Aluno> obterPorId(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

	@Override
    @Transactional(readOnly = true)
	public List<Aluno> buscarAlunos(Aluno alunoFiltro) {
	      Example example = Example.of(alunoFiltro, ExampleMatcher
	                .matching()
	                .withIgnoreCase()
	                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
	      
	        return repository.findAll(example);
	}

	@Override
	public void deletar(Aluno aluno) {
		// TODO Auto-generated method stub
		Objects.requireNonNull(aluno.getId());
        repository.delete(aluno);
	}

	@Override
	public Aluno getById(Long id) {
		return repository.getById(id);
	}

}
