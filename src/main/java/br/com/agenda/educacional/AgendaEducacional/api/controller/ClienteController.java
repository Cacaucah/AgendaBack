/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.agenda.educacional.AgendaEducacional.api.dto.ClienteDTO;
import br.com.agenda.educacional.AgendaEducacional.enums.NaturezaAula;
import br.com.agenda.educacional.AgendaEducacional.exceptions.RegraNegocioException;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Cliente;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Professor;
import br.com.agenda.educacional.AgendaEducacional.services.ClienteService;
import br.com.agenda.educacional.AgendaEducacional.services.ProfessorService;
import lombok.RequiredArgsConstructor;

/**
 * @author Ana Caroline
 *
 */
@RestController
@RequestMapping("api/instituicao")
@RequiredArgsConstructor
public class ClienteController {

	private final ClienteService service;
	private final ProfessorService professorService;

	@GetMapping
	public ResponseEntity buscar(@RequestParam(value = "nome", required = false) String nome,
			@RequestParam(value = "rua", required = false) String rua,
			@RequestParam(value = "cep", required = false) String cep,
			@RequestParam(value = "tipoAula", required = false) String tipoCliente,
			@RequestParam(value = "professor") Long idProfessor) {
		Cliente clienteFiltro = new Cliente();
		clienteFiltro.setNome(nome);
		clienteFiltro.setTipoCliente(tipoCliente);
		Optional<Professor> professor = professorService.obterPorId(idProfessor);
		if (!professor.isPresent()) {
			return ResponseEntity.badRequest()
					.body("Não foi possível realizar a consulta. Usuário não encontrado para o id informado.");
		} else {
			clienteFiltro.setProfessor(professor.get());
		}
		List<Cliente> clientes = service.buscar(clienteFiltro);
		return ResponseEntity.ok(clientes);
	}

	@GetMapping("{id}/instituicao")
	public ResponseEntity obterInstituicao(@PathVariable("id") Long id) {
		Optional<Cliente> cliente = service.obterPorId(id);
		if (!cliente.isPresent()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(cliente);
	}

	@PostMapping
	public ResponseEntity salvar(@RequestBody ClienteDTO dto) {
		try {
			Cliente instituicao = converter(dto);
			instituicao = service.salvarCliente(instituicao);
			return new ResponseEntity(instituicao, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}/atualizar-instituicao")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ClienteDTO dto) {
		return service.obterPorId(id).map(entity -> {
			try {
				Cliente cliente = new Cliente();
				cliente.setId(entity.getId());
				Professor professor = professorService.obterPorId(dto.getProfessor())
						.orElseThrow(() -> new RegraNegocioException("Professor não encontrado para o id informado"));
				cliente.setProfessor(professor);
				cliente.setNome(dto.getNome());
				cliente.setRua(dto.getRua());
				cliente.setCep(dto.getCep());
				cliente.setSituacao(dto.getSituacao());
				cliente.setTelefone(dto.getTelefone());
				cliente.setCidade(dto.getCidade());
				cliente.setNumero(dto.getNumero());
				cliente.setEstado(dto.getEstado());
				cliente.setTipoCliente(dto.getTipoCliente());
				service.atualizarCliente(cliente);
				return ResponseEntity.ok(cliente);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Cliente não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
	}

	private Cliente converter(ClienteDTO dto) {
		Cliente cliente = new Cliente();
		Professor professor = professorService.obterPorId(dto.getProfessor())
				.orElseThrow(() -> new RegraNegocioException("Professor não encontrado para o id informado"));
		cliente.setProfessor(professor);
		cliente.setNome(dto.getNome());
		cliente.setRua(dto.getRua());
		cliente.setCep(dto.getCep());
		cliente.setSituacao(dto.getSituacao());
		cliente.setTelefone(dto.getTelefone());
		cliente.setCidade(dto.getCidade());
		cliente.setNumero(dto.getNumero());
		cliente.setEstado(dto.getEstado());
		cliente.setTipoCliente(dto.getTipoCliente());
		return cliente;
	}

	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return service.obterPorId(id).map(entidade -> {
			service.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> new ResponseEntity("Materia não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}
}
