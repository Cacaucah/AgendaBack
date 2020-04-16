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

import br.com.agenda.educacional.AgendaEducacional.api.dto.MateriaDTO;
import br.com.agenda.educacional.AgendaEducacional.enums.Situacao;
import br.com.agenda.educacional.AgendaEducacional.exceptions.RegraNegocioException;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Aluno;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Materia;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Professor;
import br.com.agenda.educacional.AgendaEducacional.services.MateriaService;
import br.com.agenda.educacional.AgendaEducacional.services.ProfessorService;
import lombok.RequiredArgsConstructor;

/**
 * @author Ana Caroline
 *
 */
@RestController
@RequestMapping("api/materia")
@RequiredArgsConstructor
public class MateriaController {
	
	private final MateriaService service;
	private final ProfessorService professorService;
	@GetMapping
    public ResponseEntity buscar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam (value="professor") Long idProfessor
            ){
        Materia materiaFiltro = new Materia();
        materiaFiltro.setNome(nome);
        Optional<Professor> professor = professorService.obterPorId(idProfessor);
        if(!professor.isPresent()){
            return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para o id informado.");
        }else{
        	materiaFiltro.setProfessor(professor.get());
        }
        List<Materia> materias = service.buscarMaterias(materiaFiltro);
        return ResponseEntity.ok(materias);
    }
	@GetMapping("{id}/materia")
	public ResponseEntity obterMateria(@PathVariable("id") Long id) {
		Optional<Materia> materia = service.obterPorId(id);
		if (!materia.isPresent()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(materia);
	}
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody MateriaDTO dto) {
		try {
			Materia materia = converter(dto);
			materia = service.salvarMateria(materia);
			return new ResponseEntity(materia, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("{id}/atualizar-materia")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody MateriaDTO dto) {
		return service.obterPorId(id).map(entity -> {
			try {
				
				Materia materia = converter(dto);
				materia.setId(entity.getId());
				Professor professor = professorService.obterPorId(dto.getProfessor())
		                .orElseThrow( () -> new RegraNegocioException("Usuário não encontrado para o id informado"));
				materia.setProfessor(professor);
				materia.setNome(dto.getNome());
				service.atualizarMateria(materia);
				return ResponseEntity.ok(materia);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Materia não encontrada na base de Dados.", HttpStatus.BAD_REQUEST));
	}
	private Materia converter(MateriaDTO dto) {
		Materia materia = new Materia();
		materia.setNome(dto.getNome());
		Professor professor = professorService.obterPorId(dto.getProfessor())
	                .orElseThrow( () -> new RegraNegocioException("Professor não encontrado para o id informado"));
		materia.setProfessor(professor);
		return materia;
	}
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return service.obterPorId(id).map(entidade -> {
			service.deletarMateria(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> new ResponseEntity("Aluno não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}
}
