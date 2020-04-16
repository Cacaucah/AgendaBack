/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.agenda.educacional.AgendaEducacional.model.entity.Instituicao;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Localizacao;

/**
 * @author Ana Caroline
 *
 */
public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long>{
	
	Optional<Localizacao> findById(Long id);
}
