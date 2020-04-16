package br.com.agenda.educacional.AgendaEducacional.services;

import java.util.List;
import java.util.Optional;

import br.com.agenda.educacional.AgendaEducacional.model.entity.Aluno;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Professor;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Aluno.AlunoBuilder;

public interface AlunoService {
	
	//atualizar o aluno
    Aluno atualizarAluno(Aluno aluno);
	
	//busca um aluno por Id
	Optional<Aluno> obterPorId(Long id);
	
	//retorna uma lista de alunos
    List<Aluno> buscarAlunos(Aluno alunoFiltro);
    
    //Deleta o aluno
    void deletar(Aluno aluno);
    
    void validar(Aluno aluno);

	Aluno salvarAluno(Aluno aluno);

	public Aluno getById(Long id);


}
