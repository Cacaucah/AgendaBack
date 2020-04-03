package br.com.agenda.educacional.AgendaEducacional.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agenda.educacional.AgendaEducacional.model.entity.Aluno;


public interface AlunoRepository extends JpaRepository<Aluno, Long>{
	
	//Encontra um aluno por Id
	Optional<Aluno> findById(Long id);
		
}
