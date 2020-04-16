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

import br.com.agenda.educacional.AgendaEducacional.api.dto.ProfessorDTO;
import br.com.agenda.educacional.AgendaEducacional.exceptions.ErroDeAutenticacao;
import br.com.agenda.educacional.AgendaEducacional.exceptions.RegraNegocioException;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Professor;
import br.com.agenda.educacional.AgendaEducacional.services.ProfessorService;
import lombok.RequiredArgsConstructor;

/**
 * @author Ana Caroline
 *
 */
@RestController
@RequestMapping("api/professor")
@RequiredArgsConstructor
public class ProfessorController {
	private final ProfessorService service;

	@PostMapping
	public ResponseEntity salvar(@RequestBody ProfessorDTO dto) {

		Professor professor =  Professor.builder().nome(dto.getNome()).email(dto.getEmail()).senha(dto.getSenha())
				.build();
		try {
			Professor professorSalvo = service.salvarProfessor(professor);
			
			return new ResponseEntity(professorSalvo, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	@GetMapping("{id}/obter-professor")
	public ResponseEntity obterProfessor(@PathVariable("id") Long id) {
		Optional<Professor> professor = service.obterPorId(id);
		if (!professor.isPresent()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(professor);
	}

	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody ProfessorDTO dto) {
		try {
			Professor usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			// retorna 200, operacao realizada com sucesso
			return ResponseEntity.ok(usuarioAutenticado);
		} catch (ErroDeAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}/atualizar-professor")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ProfessorDTO dto) {
		return service.obterPorId(id).map(entity -> {
			try {
				Professor professor = converter(dto);
				if(!dto.getEmail().equals(entity.getEmail())){
					professor.setEmail(dto.getEmail());
				}else{
					professor.setEmail(entity.getEmail());
				}
				if(!dto.getEmail().equals(entity.getNome())){
					professor.setNome(dto.getNome());
				}else{
					professor.setNome(entity.getNome());
				}
				if(!dto.getSenha().equals(entity.getSenha())){
					professor.setSenha(dto.getSenha());
				}else{
					professor.setSenha(entity.getSenha());
				}
				professor.setId(entity.getId());
				service.editarProfessor(professor);
				return ResponseEntity.ok(professor);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Professor não encontrado na base de dados.", HttpStatus.BAD_REQUEST));
	}

	private Professor converter(ProfessorDTO dto) {
		Professor professor = new Professor();
		professor.setNome(dto.getNome());
		professor.setEmail(dto.getEmail());
		professor.setSenha(dto.getSenha());
		return professor;
	}
}
