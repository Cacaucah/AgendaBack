package br.com.agenda.educacional.AgendaEducacional.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agenda.educacional.AgendaEducacional.model.entity.Professor;

public interface ProfessorRepository extends JpaRepository<Professor, Long>{

	//verifica se aquele email existe e retorna um boolean
	boolean existsByEmail(String email);
	
	//Encontra um professor por Id
	Optional<Professor> findById(Long id);
	
	//Encontra um professor por email cadastrado no banco e retorna
	//um optional daquele professor
	Optional<Professor> findByEmail(String email);
}
