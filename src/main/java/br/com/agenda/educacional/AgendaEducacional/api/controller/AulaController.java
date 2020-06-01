/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.api.controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.swing.plaf.synth.SynthSeparatorUI;

import org.hibernate.exception.GenericJDBCException;
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

import br.com.agenda.educacional.AgendaEducacional.api.dto.AulaDTO;
import br.com.agenda.educacional.AgendaEducacional.api.dto.InstituicaoDTO;
import br.com.agenda.educacional.AgendaEducacional.enums.NaturezaAula;
import br.com.agenda.educacional.AgendaEducacional.exceptions.RegraNegocioException;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Aluno;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Aula;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Instituicao;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Materia;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Professor;
import br.com.agenda.educacional.AgendaEducacional.model.repository.AulaRepository;
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
	public ResponseEntity buscar(@RequestParam(value = "data", required = false) LocalDate data,
			@RequestParam(value = "professor", required = true) Long idProfessor,
			@RequestParam(value = "hora_inicial", required = true) LocalTime horaInicial,
			@RequestParam(value = "hora_fim", required = true) LocalTime horaFim
			
			) {
		Aula aulaFiltro = new Aula();
		aulaFiltro.setData(data);
		Optional<Professor> professor = professorService.obterPorId(idProfessor);
		if (!professor.isPresent()) {
			return ResponseEntity.badRequest()
					.body("Não foi possível realizar a consulta. Professor não encontrado para o dado informado.");
		} 
		aulaFiltro.setProfessor(professor.get());
	
		List<Aula> aulas = aulaService.buscar(aulaFiltro);
		if(aulas.size()>0){
			return ResponseEntity.ok(aulas);
		}else{
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
	}
	@GetMapping("{id}/aula")
	public ResponseEntity obterMateria(@PathVariable("id") Long id) {
		Optional<Aula> aula = aulaService.obterPorId(id);
		if (!aula.isPresent()) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(aula);
	}
	
	@PostMapping
	public Object salvar(@RequestBody AulaDTO dto) throws SQLException {
		Aula aula= converter(dto);
		List<Aula> aulas = aulaService.obterAulasPorHora(aula.getHoraInicial(), aula.getHoraFim(), aula.getData());
		
		try {
			if(aulas.size()>0){
				
				throw new RegraNegocioException("Não é possível agendar aula nesta faixa de horário para a data de hoje. ");
			}
			if(this.buscar(dto.getData(), dto.getProfessor(), dto.getHoraInicial(), dto.getHoraFim()).getStatusCodeValue()==200){
				throw new RegraNegocioException("Já existe uma aula agendada para esta data");
			}
				aula = (Aula) aulaService.salvarAula(aula);
				return new ResponseEntity(aula, HttpStatus.CREATED);
			
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}/atualizar-aula")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody AulaDTO dto) {
		return aulaService.obterPorId(id).map(entity -> {
			try {
				Aula aula = converter(dto);
				aula.setId(entity.getId());
				Professor professor = professorService.obterPorId(dto.getProfessor())
		                .orElseThrow( () -> new RegraNegocioException("Usuário não encontrado para o id informado"));
				aula.setProfessor(professor);
				aula.setTipoDeAula(dto.getAula());
				//buscar aluno na base para inseri-lo em aula caso houver sido informado no json
				if(dto.getAluno()!=null){
					Aluno aluno = alunoService.obterPorId(dto.getAluno())
			                .orElseThrow( () -> new RegraNegocioException("Aluno não encontrado para o id informado"));
					aula.setAluno(aluno);
				}
			
				if(dto.getInstituicao()!=null){
					Instituicao instituicao = instituicaoService.obterPorId(dto.getInstituicao())
			                .orElseThrow( () -> new RegraNegocioException("Instituição não encontrada para o id informado"));
					aula.setInstituicao(instituicao);
				}
				
				
				Materia materia = materiaService.obterPorId(dto.getMateria())
		                .orElseThrow( () -> new RegraNegocioException("Materia não encontrada para o id informado"));
				aula.setMateria(materia); 
				
				aula.setData(dto.getData());
				aula.setHoraInicial(dto.getHoraInicial());
				aula.setHoraFim(dto.getHoraFim());
				aula.setDetalhes(dto.getDetalhes());
				
				aula.setValor(dto.getValor());
				aulaService.atualizarAula(aula);
				return ResponseEntity.ok(aula);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Aula não encontrada na base de dados.", HttpStatus.BAD_REQUEST));
	}
	private Aula converter(AulaDTO dto) {
		Aula aula = new Aula();
		aula.setTipoDeAula(dto.getAula());
		aula.setData(dto.getData());
			Professor professor = professorService.obterPorId(dto.getProfessor())
	                .orElseThrow( () -> new RegraNegocioException("Professor não encontrado para o id informado."));
		aula.setProfessor(professor);
		
		if(dto.getAluno()!=null){
			Aluno aluno = alunoService.obterPorId(dto.getAluno())
	                .orElseThrow( () -> new RegraNegocioException("Aluno não encontrado para o id informado."));
		aula.setAluno(aluno);
		}
		
		if(dto.getInstituicao()!=null){
			Instituicao instituicao = instituicaoService.obterPorId(dto.getInstituicao())
					.orElseThrow( () -> new RegraNegocioException("Instituição não encontrada para o id informado."));
			aula.setInstituicao(instituicao);
		}
		
		Materia materia = materiaService.obterPorId(dto.getMateria())
					.orElseThrow( () -> new RegraNegocioException("Matéria não encontrada para o id informado."));
		aula.setMateria(materia);
		
		aula.setHoraInicial(dto.getHoraInicial());
		aula.setHoraFim(dto.getHoraFim());
		aula.setDetalhes(dto.getDetalhes());
		aula.setValor(dto.getValor());
		return aula;
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return aulaService.obterPorId(id).map(entidade -> {
			aulaService.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> new ResponseEntity("Aula não encontrada na base de Dados.", HttpStatus.BAD_REQUEST));
	}
}
