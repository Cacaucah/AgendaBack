package br.com.agenda.educacional.AgendaEducacional.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.agenda.educacional.AgendaEducacional.enums.NaturezaAula;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	Optional<Cliente> findById(Long id);

	Cliente getById(Long idInstituicao);
	
	@Query(value = "SELECT * FROM CLIENTES where professor = :idProfessor and tipo_cliente = :tipo",  nativeQuery = true)
	List<Cliente> listarTodos(
			@Param("idProfessor") Long idProfessor, @Param("tipo") NaturezaAula tipo);
}
