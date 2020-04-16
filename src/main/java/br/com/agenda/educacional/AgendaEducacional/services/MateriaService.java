package br.com.agenda.educacional.AgendaEducacional.services;

import java.util.List;
import java.util.Optional;

import br.com.agenda.educacional.AgendaEducacional.model.entity.Materia;

public interface MateriaService {
	
	Materia salvarMateria(Materia materia);
	
	Materia atualizarMateria(Materia materia);
	
	void deletarMateria(Materia materia);

	Optional<Materia> obterPorId(Long id);

    List<Materia> buscarMaterias(Materia materiaFiltro);

	Materia getById(Long idMateria);

}
