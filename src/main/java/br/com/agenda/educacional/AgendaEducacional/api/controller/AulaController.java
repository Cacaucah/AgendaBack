/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.api.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.agenda.educacional.AgendaEducacional.api.dto.AulaDTO;
import br.com.agenda.educacional.AgendaEducacional.enums.NaturezaAula;
import br.com.agenda.educacional.AgendaEducacional.exceptions.RegraNegocioException;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Aluno;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Aula;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Instituicao;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Materia;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Professor;
import br.com.agenda.educacional.AgendaEducacional.services.AlunoService;
import br.com.agenda.educacional.AgendaEducacional.services.AulaService;
import br.com.agenda.educacional.AgendaEducacional.services.InstituicaoService;
import br.com.agenda.educacional.AgendaEducacional.services.MateriaService;
import br.com.agenda.educacional.AgendaEducacional.services.ProfessorService;
import lombok.RequiredArgsConstructor;

/**
 * @author Ana Caroline
 *
 */
@RestController
@RequestMapping("api/aula")
@RequiredArgsConstructor
public class AulaController {
	private final MateriaService materiaService;
	private final AlunoService alunoService;
	private final ProfessorService professorService;
	private final InstituicaoService instituicaoService;
	private final AulaService aulaService;

	@GetMapping
	public ResponseEntity buscar(@RequestParam(value = "data_aula", required = false) LocalDate data,
			@RequestParam(value = "id_materia", required = false) Long idMateria,
			@RequestParam(value = "id_aluno", required = false) Long idAluno,
			@RequestParam(value = "id_professor", required = true) Long idProfessor,
			@RequestParam(value = "id_instituicao", required = false) Long idInstituicao) {
		Aula aulaFiltro = new Aula();
		aulaFiltro.setData(data);
		Optional<Professor> professor = professorService.obterPorId(idProfessor);
		if (!professor.isPresent()) {
			return ResponseEntity.badRequest()
					.body("Não foi possível realizar a consulta. Professor não encontrado para o dado informado.");
		} else {
			aulaFiltro.setProfessor(professor.get());
		}
		Optional<Aluno> aluno = alunoService.obterPorId(idAluno);
		if (!aluno.isPresent()) {
			return ResponseEntity.badRequest()
					.body("Não foi possível realizar a consulta. Aluno não encontrado para o dado informado.");
		} else {
			aulaFiltro.setAluno(aluno.get());
		}
		Optional<Materia> materia = materiaService.obterPorId(idMateria);
		if (!materia.isPresent()) {
			return ResponseEntity.badRequest()
					.body("Não foi possível realizar a consulta. Materia não encontrada para o dado informado.");
		} else {
			aulaFiltro.setMateria(materia.get());
		}
		Optional<Instituicao> instituicao = instituicaoService.obterPorId(idInstituicao);
		Instituicao ins = instituicaoService.getById(idInstituicao);
		if (!instituicao.isPresent()) {
			return ResponseEntity.badRequest()
					.body("Não foi possível realizar a consulta. Insituicao não encontrada para o dado informado.");
		} else {
			aulaFiltro.setInstituicao(instituicao.get());
		}
		List<Aula> aulas = aulaService.buscar(aulaFiltro);
		return ResponseEntity.ok(aulas);
	}

	@PostMapping
	public ResponseEntity salvar(@RequestBody AulaDTO dto) {
		try {
			Aula aula= converter(dto);
			
			System.out.println(aula);
			//aula = aulaService.salvarAula(aula);
			return new ResponseEntity(aula, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	private Aula converter(AulaDTO dto) {
		Aula aula = new Aula();
		aula.setTipoDeAula(dto.getAula());
		aula.setData(dto.getDatas());
			Professor professor = professorService.obterPorId(dto.getProfessor())
	                .orElseThrow( () -> new RegraNegocioException("Professor não encontrado para o id informado."));
		aula.setProfessor(professor);
		
			Aluno aluno = alunoService.obterPorId(dto.getAluno())
	                .orElseThrow( () -> new RegraNegocioException("Aluno não encontrado para o id informado."));
		aula.setAluno(aluno);
		
		
			Instituicao instituicao = instituicaoService.obterPorId(dto.getInstituicao())
					.orElseThrow( () -> new RegraNegocioException("Instituição não encontrada para o id informado."));
			aula.setInstituicao(instituicao);
		
		
			Materia materia = materiaService.obterPorId(dto.getMateria())
					.orElseThrow( () -> new RegraNegocioException("Matéria não encontrada para o id informado."));
			aula.setMateria(materia);
		
		aula.setHora(dto.getHora());
		aula.setDetalhes(dto.getDetalhes());
		aula.setValor(dto.getValor());
		return aula;
	}
}
