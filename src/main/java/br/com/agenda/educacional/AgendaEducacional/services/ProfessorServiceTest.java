package br.com.agenda.educacional.AgendaEducacional.services;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.agenda.educacional.AgendaEducacional.exceptions.RegraNegocioException;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Professor;
import br.com.agenda.educacional.AgendaEducacional.model.repository.ProfessorRepository;
import br.com.agenda.educacional.AgendaEducacional.services.impl.ProfessorServiceImplementacao;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ProfessorServiceTest {

	@SpyBean
	ProfessorServiceImplementacao service;

	// o mockbean é para mocar uma instancia a fazendo "fake"
	@MockBean
	ProfessorRepository repository;

	@Test
	public void deveSalvarProfessor() {
		Professor professorASalvar = criarProfessor();
		Mockito.doNothing().when(service).validarEmail(professorASalvar.getEmail());
		Professor professorSalvo = criarProfessor();
		professorSalvo.setId(1l);
		professorSalvo.setNome("Douglas");
		professorSalvo.setEmail("doug@gmail.com");
		professorSalvo.setSenha("123");

		// ao chamar o metodo salvar ele retorna o institutoSalvo
		Mockito.when(repository.save(professorASalvar)).thenReturn(professorSalvo);

		Professor professor = service.salvarProfessor(professorASalvar);

		// Assertions that quer dizer: verifique que
		Assertions.assertThat(professor.getId()).isEqualTo(professorSalvo.getId());
		Assertions.assertThat(professor.getNome()).isEqualTo(professorSalvo.getNome());
		Assertions.assertThat(professor.getEmail()).isEqualTo(professorSalvo.getEmail());
		Assertions.assertThat(professor.getSenha()).isEqualTo(professorSalvo.getSenha());
	}

	@Test
	public void naoDeveSalvarUmProfessorQuandoHouverErroDeValidacao() {
		// cenario
		Professor professorASalvar = criarProfessor();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(professorASalvar.getEmail());

		// execucacao verificacao
		Assertions.catchThrowableOfType(() -> service.salvarProfessor(professorASalvar), RegraNegocioException.class);

		Mockito.verify(repository, Mockito.never()).save(professorASalvar);
	}

	private Professor criarProfessor() {
		return Professor.builder().nome("Douglas").email("doug@gmail.com").senha("123").build();
	}
	
	 @Test
	    public void deveAtualizarUmProfessor(){
	        //cenario
	        Professor professorSalvo = criarProfessor();
	        professorSalvo.setId(1l);

	        Mockito.doNothing().when(service).validarEmail(professorSalvo.getEmail());

	        Mockito.when(repository.save(professorSalvo)).thenReturn(professorSalvo);

	        //execucao
	        service.editarProfessor(professorSalvo);

	        //verifique que o repository chamou o método save ao menos uma vez com o lancamentoSalvo
	        Mockito.verify(repository, Mockito.times(1)).save(professorSalvo);
	    }
	 
	    @Test
	    public void deveObterUmProfessorPorID(){
	        //cenario
	        Long id = 1l;
	        Professor professor = criarProfessor();
	        professor.setId(id);

	        Mockito.when(repository.findById(id)).thenReturn(Optional.of(professor));

	        //execucao
	        Optional<Professor> resultado = service.obterPorId(id);

	        //verificacao
	        Assertions.assertThat(resultado.isPresent()).isTrue();
	    }
}
