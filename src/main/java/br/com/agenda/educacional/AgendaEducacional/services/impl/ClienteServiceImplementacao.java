/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.services.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import br.com.agenda.educacional.AgendaEducacional.enums.NaturezaAula;
import br.com.agenda.educacional.AgendaEducacional.exceptions.RegraNegocioException;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Cliente;
import br.com.agenda.educacional.AgendaEducacional.model.repository.ClienteRepository;
import br.com.agenda.educacional.AgendaEducacional.services.ClienteService;

/**
 * @author Ana Caroline
 *
 */
@Service
public class ClienteServiceImplementacao implements ClienteService{

	private final ClienteRepository repository;

	public ClienteServiceImplementacao(ClienteRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public Cliente salvarCliente(Cliente cliente) {
		validar(cliente);
		return repository.save(cliente);
	}

	public void validar(Cliente cliente) {
		if(cliente.getId()==null && cliente.getNome().trim().equals("")){
			throw new RegraNegocioException("Descrição inválida.");
		}
		if(cliente.getRua().trim().equals("")){
			throw new RegraNegocioException("Informe uma rua.");
		}
		if(Long.toString(cliente.getNumero()).equals("")){
			throw new RegraNegocioException("Informe um numero.");
		}
		
	}

	@Override
	public Cliente atualizarCliente(Cliente cliente) {
		//verifica se o objeto que chega não é nullo
		Objects.requireNonNull(cliente.getId());
		//se não for ele passa novamente pela validação
		validar(cliente);
		// se for valido, então salva a instituicao
		return repository.save(cliente);
	}

	@Override
	public void deletar(Cliente cliente) {
		// TODO Auto-generated method stub
		Objects.requireNonNull(cliente.getId());
		repository.delete(cliente);
	}

	@Override
	public Optional<Cliente> obterPorId(Long id) {
		return repository.findById(id);
	}

	@Override
	public List<Cliente> buscar(Cliente clientesFiltro) {
		Example example = Example.of(clientesFiltro,
				ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

		return repository.findAll(example);
	}

	@Override
	public Cliente getById(Long idCliente) {
		return repository.getById(idCliente);
	}

	@Override
	public List<Cliente> listarTodos(Long idProfessor, NaturezaAula tipo) {
		// TODO Auto-generated method stub
		return repository.listarTodos(idProfessor, tipo);
	}

//	@Override
//	public List<Cliente> obterClientes(NaturezaAula natureza) {
//		return repository.obterClientes(natureza);
//	}

}
