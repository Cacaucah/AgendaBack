/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.agenda.educacional.AgendaEducacional.enums.Situacao;
import br.com.agenda.educacional.AgendaEducacional.exceptions.RegraNegocioException;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Aluno;
import br.com.agenda.educacional.AgendaEducacional.model.repository.AlunoRepository;
import br.com.agenda.educacional.AgendaEducacional.services.impl.AlunoServiceImplementacao;
/**
 * @author Ana Caroline
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")

//as anotações acima indicam como o java irá rodar esse arquivo e com qual perfil, nesse
//caso será o perfil de tests informado em src/main/resources application-test.properties
//esse arquivo aponta para um banco em memória, o h2database
public class AlunoServiceTest {

	//com o spybean é possível realizar os testes utilizando os métodos reais da aplicação
	@SpyBean
    AlunoServiceImplementacao service;

	//o mockbean é para mocar uma instancia a fazendo "fake"
    @MockBean
    AlunoRepository repository;
    
    @Test
    public void deveSalvarUmAluno(){
    		//cenario de teste
    		Aluno alunoASalvar = criarUmAluno();
    		Mockito.doNothing().when(service).validar(alunoASalvar);
    		Aluno alunoSalvo = criarUmAluno();
    		alunoSalvo.setId(1l);
    		alunoSalvo.setNome("Felipe");
    		alunoSalvo.setSituacao(Situacao.ATIVO);
    		Mockito.when(repository.save(alunoASalvar)).thenReturn(alunoSalvo);
    		
    		//execucao
    		Aluno aluno = service.salvarAluno(alunoASalvar);
    		
    		//Assertions that quer dizer: verifique que
    		Assertions.assertThat(aluno.getId()).isEqualTo(alunoSalvo.getId());
    		Assertions.assertThat(aluno.getNome()).isEqualTo(alunoSalvo.getNome());
    		Assertions.assertThat(aluno.getSituacao()).isEqualTo(alunoSalvo.getSituacao());

    }
    
    @Test
    public void naoDeveSalvarUmAlunoQuandoHouverErroDeValidacao(){
    	Aluno alunoASalvar = criarUmAluno();
    	Mockito.doThrow(RegraNegocioException.class).when(service).validar(alunoASalvar);
    	
    	Assertions.catchThrowableOfType(()->service.salvarAluno(alunoASalvar), RegraNegocioException.class);
    	
    	Mockito.verify(repository, Mockito.never()).save(alunoASalvar);
    }
    
    @Test
    public void deveAtualizarUmAluno(){
		Aluno alunoSalvo = criarUmAluno();
		alunoSalvo.setId(1l);
		alunoSalvo.setSituacao(Situacao.INATIVO);
		
        Mockito.doNothing().when(service).validar(alunoSalvo);
        
        service.atualizarAluno(alunoSalvo);

        //verifique que o repository chamou o método save ao menos uma vez com o lancamentoSalvo
        Mockito.verify(repository, Mockito.times(1)).save(alunoSalvo);
    }
    
    @Test 
    public void deveLancarErroAoTentarAtualizarUmAlunoQueNaoExiste(){
    	Aluno alunoASalvar = criarUmAluno();

    	  Assertions.catchThrowableOfType(()->service.atualizarAluno(alunoASalvar), NullPointerException.class);

          Mockito.verify(repository, Mockito.never()).save(alunoASalvar);
    }
    
    @Test public void deveDeletarUmAluno(){
		Aluno aluno = criarUmAluno();
		aluno.setId(1l);
		
		service.deletar(aluno);
		
        Mockito.verify(repository).delete(aluno);

    }
    @Test public void deveLancarErroAoDeletarUmAlunoQueNaoExiste(){
		Aluno aluno = criarUmAluno();

        Assertions.catchThrowableOfType(()->service.deletar(aluno), NullPointerException.class);
        
        Mockito.verify(repository, Mockito.never()).delete(aluno);

    }
    @Test
    public void deveFiltrarAluno(){
		Aluno aluno = criarUmAluno();
		aluno.setId(1l);
		
		List<Aluno> lista = Arrays.asList(aluno);
		
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
        List<Aluno> resultado = service.buscarAlunos(aluno);

		Assertions.assertThat(resultado)
			        .isNotEmpty()
			        .hasSize(1)
			        .contains(aluno);
    }
    @Test
    public void deveAtualizarASituacaoDeUmAluno(){
    	Aluno aluno = criarUmAluno();
		aluno.setId(1l);
		aluno.setSituacao(Situacao.INATIVO);

        Situacao novaSituacao = Situacao.INATIVO;
        Mockito.doReturn(aluno).when(service).atualizarAluno(aluno);

        //execucacao
        service.atualizarAluno(aluno);

        Assertions.assertThat(aluno.getSituacao()).isEqualTo(novaSituacao);
        Mockito.verify(service).atualizarAluno(aluno);

    }
    @Test
    public void deveObterUmAlunoPorId(){
    	Long id = 1l;
    	Aluno aluno = criarUmAluno();
		aluno.setId(id);
		
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(aluno));
        
        Optional<Aluno> resultado = service.obterPorId(id);
        
        Assertions.assertThat(resultado.isPresent()).isTrue();
    }
    
    @Test
    public void deveRetornarVazioQuandoOAlunoNaoExistir(){
    	Long id = 1l;
    	Aluno aluno = criarUmAluno();
		aluno.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		
        Optional<Aluno> resultado = service.obterPorId(id);

        Assertions.assertThat(resultado.isPresent()).isFalse();
    }
    
    @Test
    public void deveLancarErroAoValidarAluno(){
    	Aluno aluno = new Aluno();
    	
    	aluno.setNome("");
    	Throwable erro  = Assertions.catchThrowable( ()-> service.validar(aluno));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe os dados corretamente!");

        aluno.setId(1l);
        aluno.setNome("");
        erro  = Assertions.catchThrowable( ()-> service.validar(aluno));
        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe o nome do aluno.");

    }
    //cria um aluno
    public static Aluno criarUmAluno(){
    	return Aluno.builder()
    						.nome("Felipe")
    						.situacao(Situacao.ATIVO).build();
    }
}
