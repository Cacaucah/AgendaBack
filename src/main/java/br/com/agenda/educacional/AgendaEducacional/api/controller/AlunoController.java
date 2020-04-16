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
import br.com.agenda.educacional.AgendaEducacional.enums.Situacao;
import br.com.agenda.educacional.AgendaEducacional.exceptions.RegraNegocioException;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Aluno;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Professor;
import br.com.agenda.educacional.AgendaEducacional.services.AlunoService;
import br.com.agenda.educacional.AgendaEducacional.services.ProfessorService;
import lombok.RequiredArgsConstructor;

/**
 * @author Ana Caroline
 *
 */
@RestController
@RequestMapping("api/aluno")
@RequiredArgsConstructor
public class AlunoController {

	private final AlunoService service;
	private final ProfessorService professorService;
	
	@GetMapping
    public ResponseEntity buscar(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "situacao", required = false) String situacao,
            @RequestParam (value="professor") Long idProfessor
            ){
        Aluno alunoFiltro = new Aluno();
        alunoFiltro.setNome(nome);
        if(situacao.equals("ATIVO")){
            alunoFiltro.setSituacao(Situacao.ATIVO);
        }else{
            alunoFiltro.setSituacao(Situacao.INATIVO);
        }
        Optional<Professor> professor = professorService.obterPorId(idProfessor);
        if(!professor.isPresent()){
            return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para o id informado.");
        }else{
        	alunoFiltro.setProfessor(professor.get());
        }
        List<Aluno> alunos = service.buscarAlunos(alunoFiltro);
        return ResponseEntity.ok(alunos);
    }
	
	@GetMapping("{id}/obter-aluno")
	public ResponseEntity obterAluno(@PathVariable("id") Long id) {
		Optional<Aluno> aluno = service.obterPorId(id);
		if (!aluno.isPresent()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(aluno);
	}

	@PostMapping
	public ResponseEntity salvar(@RequestBody AlunoDTO dto) {
		try {
			Aluno aluno = converter(dto);
			aluno = service.salvarAluno(aluno);
			return new ResponseEntity(aluno, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("{id}/atualizar-aluno")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody AlunoDTO dto) {
		return service.obterPorId(id).map(entity -> {
			try {
				Aluno aluno = converter(dto);
				aluno.setId(entity.getId());
				aluno.setNome(dto.getNome());
				aluno.setSituacao(dto.getSituacao());
				Professor professor = professorService.obterPorId(dto.getProfessor())
		                .orElseThrow( () -> new RegraNegocioException("Professor não encontrado para o id informado"));
				aluno.setProfessor(professor);
				service.atualizarAluno(aluno);
				return ResponseEntity.ok(aluno);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Aluno não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}

	private Aluno converter(AlunoDTO dto) {
		Aluno aluno = new Aluno();
		aluno.setNome(dto.getNome());
		aluno.setSituacao(dto.getSituacao());
		Professor professor = professorService.obterPorId(dto.getProfessor())
	                .orElseThrow( () -> new RegraNegocioException("Professor não encontrado para o id informado"));
		aluno.setProfessor(professor);
		return aluno;
	}

	@DeleteMapping("{id}/deletar")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return service.obterPorId(id).map(entidade -> {
			service.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> new ResponseEntity("Aluno não encontrado na base de Dados.", HttpStatus.BAD_REQUEST));
	}
}
