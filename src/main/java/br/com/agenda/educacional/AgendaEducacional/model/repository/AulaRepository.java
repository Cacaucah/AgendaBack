package br.com.agenda.educacional.AgendaEducacional.model.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.agenda.educacional.AgendaEducacional.model.entity.Aula;

public interface AulaRepository extends JpaRepository<Aula, Long>{

	Optional<Aula> findById(Long id);
	
	@Query(value = "from Aula where horaInicial between :horaInicial and :horaFim and data = :data")
	List<Aula> obterAulasDentroDeUmIntervalo(
			@Param("horaInicial") LocalTime horaInicial,
			@Param("horaFim") LocalTime horaFinal,
			@Param("data") LocalDate data
			);
}
