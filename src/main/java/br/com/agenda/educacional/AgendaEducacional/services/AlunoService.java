package br.com.agenda.educacional.AgendaEducacional.services;

import java.util.List;
import java.util.Optional;

import br.com.agenda.educacional.AgendaEducacional.model.entity.Aluno;

public interface AlunoService {
	
	Aluno salvarAluno(Aluno aluno);
	
	//atualizar o aluno
    Aluno atualizarAluno(Aluno aluno);
	
	//busca um aluno por Id
	Optional<Aluno> obterPorId(Long id);
	
	//retorna uma lista de alunos
    List<Aluno> buscarAlunos(Aluno alunoFiltro);
    
    //Deleta o aluno
    void deletar(Aluno aluno);


}
