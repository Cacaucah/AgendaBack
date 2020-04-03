package br.com.agenda.educacional.AgendaEducacional.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agenda.educacional.AgendaEducacional.model.entity.Aula;

public interface AulaRepository extends JpaRepository<Aula, Long>{

	Optional<Aula> findById(Long id);
}
