package br.com.agenda.educacional.AgendaEducacional.services.impl;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.agenda.educacional.AgendaEducacional.exceptions.ErroDeAutenticacao;
import br.com.agenda.educacional.AgendaEducacional.exceptions.RegraNegocioException;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Professor;
import br.com.agenda.educacional.AgendaEducacional.model.repository.ProfessorRepository;
import br.com.agenda.educacional.AgendaEducacional.services.ProfessorService;

@Service
//Implementacao do service de Professor
public class ProfessorServiceImplementacao implements ProfessorService {

	private ProfessorRepository repository;
	
	@Autowired 
	public ProfessorServiceImplementacao(ProfessorRepository repository) {
		super();
		this.repository = repository;
	}

	
	@Override
	public Professor autenticar(String email, String senha) {
		Optional<Professor> professor = repository.findByEmail(email);

		if(!professor.isPresent()) {
			throw new ErroDeAutenticacao("Usuário não encontrado para o email informado.");
		}
		if(!professor.get().getSenha().equals(senha)){
			throw new ErroDeAutenticacao("Senha inválida.");
		}
		//se passar, retorna uma instancia do professor logado
		return professor.get();
	}

	@Override
	public Professor salvarProfessor(Professor professor) {
		// TODO Auto-generated method stub
		validarEmail(professor.getEmail());
		
		//retorna a instancia do usuario persistida na base, já com o id
		return repository.save(professor);
	}

	@Override
	public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe){
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
		}		
	}


	@Override
	public Optional<Professor> obterPorId(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}


	@Override
	public Professor editarProfessor(Professor professor) {
		Objects.requireNonNull(professor.getId());
		validarEmail(professor.getEmail());
		// se for valida, então salva o professor
		return repository.save(professor);
	}


	@Override
	public Professor getById(Long id) {
		return repository.getById(id);
	}

}
