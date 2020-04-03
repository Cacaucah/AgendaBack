package br.com.agenda.educacional.AgendaEducacional.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agenda.educacional.AgendaEducacional.model.entity.Instituicao;

public interface InstituicaoRepository extends JpaRepository<Instituicao, Long> {

	Optional<Instituicao> findById(Long id);
}
