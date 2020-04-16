package br.com.agenda.educacional.AgendaEducacional.services;

import java.util.Optional;

import br.com.agenda.educacional.AgendaEducacional.model.entity.Professor;

public interface ProfessorService {

	//autentica o professor
	Professor autenticar(String email, String senha);
	
	//Salva na base de dados
	Professor salvarProfessor(Professor professor);
	
	Professor editarProfessor(Professor professor);

	//valida o email
	void validarEmail(String email);
	
	public Optional<Professor> obterPorId(Long id);
	
	public Professor getById(Long id);


}
