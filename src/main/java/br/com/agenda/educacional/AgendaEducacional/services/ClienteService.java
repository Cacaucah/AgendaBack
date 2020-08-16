package br.com.agenda.educacional.AgendaEducacional.services;

import java.util.List;
import java.util.Optional;

import br.com.agenda.educacional.AgendaEducacional.enums.NaturezaAula;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Cliente;

public interface ClienteService {

	Cliente salvarCliente(Cliente instituicao);
	
	Cliente atualizarCliente(Cliente instituicao);
	
	void deletar(Cliente instituicao);
	
    Optional<Cliente> obterPorId(Long id);
    
    List<Cliente> buscar(Cliente instuicoesFiltro);

    List<Cliente> listarTodos(Long idProfessor, NaturezaAula tipo);
    
    
    Cliente getById(Long idInstituicao);


}
