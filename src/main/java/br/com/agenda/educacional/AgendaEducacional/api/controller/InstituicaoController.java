/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.agenda.educacional.AgendaEducacional.api.dto.AlunoDTO;
import br.com.agenda.educacional.AgendaEducacional.api.dto.InstituicaoDTO;
import br.com.agenda.educacional.AgendaEducacional.api.dto.ProfessorDTO;
import br.com.agenda.educacional.AgendaEducacional.exceptions.RegraNegocioException;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Aluno;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Instituicao;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Materia;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Professor;
import br.com.agenda.educacional.AgendaEducacional.services.InstituicaoService;
import br.com.agenda.educacional.AgendaEducacional.services.ProfessorService;
import lombok.RequiredArgsConstructor;

/**
 * @author Ana Caroline
 *
 */
@RestController
@RequestMapping("api/instituicao")
@RequiredArgsConstructor
public class InstituicaoController {

	private final InstituicaoService service;
	private final ProfessorService professorService;

	@GetMapping
	public ResponseEntity buscar(@RequestParam(value = "nome", required = false) String nome,
			@RequestParam(value = "rua", required = false) String rua,
			@RequestParam(value = "cep", required = false) String cep,
			@RequestParam(value = "professor") Long idProfessor) {
		Instituicao instituicaoFiltro = new Instituicao();
		instituicaoFiltro.setNome(nome);
		Optional<Professor> professor = professorService.obterPorId(idProfessor);
		if (!professor.isPresent()) {
			return ResponseEntity.badRequest()
					.body("Não foi possível realizar a consulta. Usuário não encontrado para o id informado.");
		} else {
			instituicaoFiltro.setProfessor(professor.get());
		}
		List<Instituicao> instituicoes = service.buscar(instituicaoFiltro);
		return ResponseEntity.ok(instituicoes);
	}

	@GetMapping("{id}/instituicao")
	public ResponseEntity obterInstituicao(@PathVariable("id") Long id) {
		Optional<Instituicao> instituicao = service.obterPorId(id);
		if (!instituicao.isPresent()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(instituicao);
	}

	@PostMapping
	public ResponseEntity salvar(@RequestBody InstituicaoDTO dto) {
		try {
			Instituicao instituicao = converter(dto);
			instituicao = service.salvarInstituicao(instituicao);
			return new ResponseEntity(instituicao, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}/atualizar-instituicao")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody InstituicaoDTO dto) {
		return service.obterPorId(id).map(entity -> {
			try {
				Instituicao instituicao = converter(dto);
				instituicao.setId(entity.getId());
				Professor professor = professorService.obterPorId(dto.getProfessor())
						.orElseThrow(() -> new RegraNegocioException("Professor não encontrado para o id informado"));
				instituicao.setProfessor(professor);
				instituicao.setNome(dto.getNome());
				instituicao.setRua(dto.getRua());
				instituicao.setCep(dto.getCep());
				instituicao.setSituacao(dto.getSituacao());
				instituicao.setTelefone(dto.getTelefone());
				instituicao.setCidade(dto.getCidade());
				instituicao.setNumero(dto.getNumero());
				instituicao.setEstado(dto.getEstado());
				service.atualizarInstituicao(instituicao);
				return ResponseEntity.ok(instituicao);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Instituicao não encontrada na base de dados.", HttpStatus.BAD_REQUEST));
	}

	private Instituicao converter(InstituicaoDTO dto) {
		Instituicao instituicao = new Instituicao();
		Professor professor = professorService.obterPorId(dto.getProfessor())
				.orElseThrow(() -> new RegraNegocioException("Professor não encontrado para o id informado"));
		instituicao.setProfessor(professor);
		instituicao.setNome(dto.getNome());
		instituicao.setRua(dto.getRua());
		instituicao.setCep(dto.getCep());
		instituicao.setSituacao(dto.getSituacao());
		instituicao.setTelefone(dto.getTelefone());
		instituicao.setCidade(dto.getCidade());
		instituicao.setNumero(dto.getNumero());
		instituicao.setEstado(dto.getEstado());
		return instituicao;
	}

	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return service.obterPorId(id).map(entidade -> {
			service.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> new ResponseEntity("Materia não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}
}
