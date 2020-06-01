package br.com.agenda.educacional.AgendaEducacional.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import br.com.agenda.educacional.AgendaEducacional.model.entity.Aula;

public interface AulaService {

	Object salvarAula(Aula aula);

	Aula atualizarAula(Aula aula);
	
	void deletar(Aula aula);
	
  Optional<Aula> obterPorId(Long id);
 
  List<Aula> buscar(Aula aulasFiltro);
  List<Aula> obterAulasPorHora(LocalTime horaInicial, LocalTime horaFinal, LocalDate data);

}
