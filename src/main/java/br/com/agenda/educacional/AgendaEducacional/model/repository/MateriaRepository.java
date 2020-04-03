package br.com.agenda.educacional.AgendaEducacional.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agenda.educacional.AgendaEducacional.model.entity.Materia;

public interface MateriaRepository extends JpaRepository<Materia, Long>{

	//Encontra uma materia por Id
	Optional<Materia> findById(Long id);
}
