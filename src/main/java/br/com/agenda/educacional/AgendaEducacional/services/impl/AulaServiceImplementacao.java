package br.com.agenda.educacional.AgendaEducacional.services.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.agenda.educacional.AgendaEducacional.enums.NaturezaAula;
import br.com.agenda.educacional.AgendaEducacional.exceptions.RegraNegocioException;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Aula;
import br.com.agenda.educacional.AgendaEducacional.model.repository.AulaRepository;
import br.com.agenda.educacional.AgendaEducacional.services.AulaService;

@Service
public class AulaServiceImplementacao implements AulaService {

	private final AulaRepository repository;

	public AulaServiceImplementacao(AulaRepository repository) {
		this.repository = repository;
	}
	@Override
	@Transactional
	public List<Aula> obterAulasPorHora(LocalTime horaInicial, LocalTime horaFinal, LocalDate data){
		return repository.obterAulasDentroDeUmIntervalo(horaInicial,  horaFinal, data);
	}
	@Override
	@Transactional
	public Aula salvarAula(Aula aula) {
		// TODO Auto-generated method stub
		validar(aula);
		return repository.save(aula);
		
	}

	@Override
	@Transactional
	public Aula atualizarAula(Aula aula) {
		Objects.requireNonNull(aula.getId());
		validar(aula);
		// se for valida, então salva a aula
		return repository.save(aula);
	}

	public void validar(Aula aula) {
		
		if(aula.getDetalhes().trim().equals("")){
			throw new RegraNegocioException("Descrição inválida.");
		}
		// se o tipo da aula for particular e o valor informado for menor que
		// zero, lançar excpetion
//		if (aula.getTipoDeAula().equals(NaturezaAula.PARTICULAR) && aula.getValor().compareTo(BigDecimal.ZERO) < 1) {
//			throw new RegraNegocioException("Informe um Valor válido.");
//		}
		
	}
	//retorna a data atual
	public Date dataAtual(){
		Date d = new Date();

		Calendar cal = new GregorianCalendar();

		cal.setTime(d);
		return cal.getTime();
	}
	@Override
	@Transactional
	public void deletar(Aula aula) {
		
		// verifica se o objeto que chega não é nullo
		Objects.requireNonNull(aula.getId());
		//validar no front o delete
		repository.delete(aula);
	}

	@Override
	public Optional<Aula> obterPorId(Long id) {
		return repository.findById(id);
	}

	@Override
	public List<Aula> buscar(Aula aulasFiltro) {
		Example example = Example.of(aulasFiltro,
				ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

		return repository.findAll(example);
	}

}
