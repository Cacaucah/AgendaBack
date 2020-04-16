/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.api.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
		Instituicao instituicao = Instituicao.builder().nome(dto.getNome()).rua(dto.getRua()).numero(dto.getNumero()).cep(dto.getCep()).build();
		try {
			Instituicao instituicaoSalva = service.salvarInstituicao(instituicao);
			return new ResponseEntity(instituicaoSalva, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@PutMapping("{id}/atualizar-instituicao")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody InstituicaoDTO dto) {
		return service.obterPorId(id).map(entity -> {
			try {
				Instituicao instituicao = converter(dto);
				if(!dto.getNome().equals(entity.getNome())){
					instituicao.setNome(dto.getNome());
				}else{
					instituicao.setNome((entity.getNome()));
				}
				if(!dto.getRua().equals(entity.getRua())){
					instituicao.setRua(dto.getRua());
				}else{
					instituicao.setRua(entity.getRua());
				}
				if(!dto.getNumero().equals(entity.getNumero())){
					instituicao.setNumero(dto.getNumero());
				}else{
					instituicao.setNumero(entity.getNumero());
				}
				if(!dto.getCep().equals(entity.getCep())){
					instituicao.setCep(dto.getCep());
				}else{
					instituicao.setCep(entity.getCep());
				}
				instituicao.setId(entity.getId());
				service.atualizarInstituicao(instituicao);
				return ResponseEntity.ok(instituicao);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Instituicao não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
	}
	private Instituicao converter(InstituicaoDTO dto) {
		Instituicao instituicao = new Instituicao();
		instituicao.setNome(dto.getNome());
		instituicao.setRua(dto.getRua());
		instituicao.setNumero(dto.getNumero());
		instituicao.setCep(dto.getCep());
		return instituicao;
	}
}
