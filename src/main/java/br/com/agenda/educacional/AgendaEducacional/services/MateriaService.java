package br.com.agenda.educacional.AgendaEducacional.services;

import java.util.List;

import br.com.agenda.educacional.AgendaEducacional.model.entity.Materia;

public interface MateriaService {
	
	Materia salvarMateria(Materia materia);
	
	Materia atualizarMateria(Materia materia);
	
	Materia deletarMateria(Materia materia);

    List<Materia> buscarMaterias(Materia materiaFiltro);

}
