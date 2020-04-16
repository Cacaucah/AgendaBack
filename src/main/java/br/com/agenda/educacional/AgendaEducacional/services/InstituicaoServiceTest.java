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

import br.com.agenda.educacional.AgendaEducacional.exceptions.RegraNegocioException;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Instituicao;
import br.com.agenda.educacional.AgendaEducacional.model.repository.InstituicaoRepository;
import br.com.agenda.educacional.AgendaEducacional.services.impl.InstituicaoServiceImplementacao;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")

//as anotações acima indicam como o java irá rodar esse arquivo e com qual perfil, nesse
//caso será o perfil de tests informado em src/main/resources application-test.properties
//esse arquivo aponta para um banco em memória, o h2database
public class InstituicaoServiceTest {

	//com o spybean é possível realizar os testes utilizando os métodos reais da aplicação
		@SpyBean
	    InstituicaoServiceImplementacao service;

		//o mockbean é para mocar uma instancia a fazendo "fake"
	    @MockBean
	    InstituicaoRepository repository;
	    
	    @Test
	    public void deveSalvarInstituicao(){
	    	Instituicao institutoASalvar = criarInstituicao();
	    	Mockito.doNothing().when(service).validar(institutoASalvar);
	    	Instituicao institutoSalvo = criarInstituicao();
	    	institutoSalvo.setId(1l);
	    	institutoSalvo.setNome("FAESP");
	    	institutoSalvo.setRua("Pedro Gusso");
	    	institutoSalvo.setCep("10906887");
	    	institutoSalvo.setNumero(123l);
	    	
	    	//ao chamar o metodo salvar ele retorna o institutoSalvo
	    	Mockito.when(repository.save(institutoASalvar)).thenReturn(institutoSalvo);
	    	
	    	Instituicao instituicao = service.salvarInstituicao(institutoASalvar);
	    	
	    	//Assertions that quer dizer: verifique que
    		Assertions.assertThat(instituicao.getId()).isEqualTo(institutoSalvo.getId());
    		Assertions.assertThat(instituicao.getNome()).isEqualTo(institutoSalvo.getNome());
    		Assertions.assertThat(instituicao.getRua()).isEqualTo(institutoSalvo.getRua());
    		Assertions.assertThat(instituicao.getCep()).isEqualTo(institutoSalvo.getCep());
    		Assertions.assertThat(instituicao.getNumero()).isEqualTo(institutoSalvo.getNumero());

	    }
	    @Test
	    public void naoDeveSalvarInstituicaoQuandoHouverErroDeValidacao(){
	        //cenario
	    	Instituicao institutoASalvar = criarInstituicao();
	        Mockito.doThrow(RegraNegocioException.class).when(service).validar(institutoASalvar);

	        //execucacao verificacao
	        Assertions.catchThrowableOfType(()->service.salvarInstituicao(institutoASalvar), RegraNegocioException.class);

	        Mockito.verify(repository, Mockito.never()).save(institutoASalvar);
	    }
	    @Test
	    public void deveAtualizarUmaInstituicao(){
	    	Instituicao institutoSalvo = criarInstituicao();
	    	institutoSalvo.setId(1l);
	    	institutoSalvo.setCep("015023698");
	        Mockito.doNothing().when(service).validar(institutoSalvo);
	        
	        Mockito.when(repository.save(institutoSalvo)).thenReturn(institutoSalvo);
	        
	        service.atualizarInstituicao(institutoSalvo);

	        Mockito.verify(repository, Mockito.times(1)).save(institutoSalvo);

	    }
	    @Test
	    public void deveLancarErroAoTentarAtualizarUmaInstituicaoNaoSalva(){
	    	Instituicao institutoASalvar = criarInstituicao();
	        Assertions.catchThrowableOfType(()->service.atualizarInstituicao(institutoASalvar), NullPointerException.class);

	        Mockito.verify(repository, Mockito.never()).save(institutoASalvar);

	    }
	    @Test
	    public void deveDeletarInstituicao(){
	    	Instituicao instituicao = criarInstituicao();
	    	instituicao.setId(1l);
	        service.deletar(instituicao);

	        Mockito.verify(repository).delete(instituicao);
	    }
	    @Test
	    public void deveLancarErroAoDeletarUmLancamentoQueNaoFoiSalvo(){
	    	Instituicao instituicao = criarInstituicao();
	        Assertions.catchThrowableOfType(()->service.deletar(instituicao), NullPointerException.class);
	        
	        //verificacao
	        Mockito.verify(repository, Mockito.never()).delete(instituicao);

	    }
	    @Test
	    public void deveFiltrarInsituicao(){
	        //cenario
	    	Instituicao instituicao = criarInstituicao();
	    	instituicao.setId(1l);

	        List<Instituicao> lista = Arrays.asList(instituicao);
	        Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);

	        //execucacao
	        List<Instituicao> resultado = service.buscar(instituicao);

	        //verificacoes
	        Assertions.assertThat(resultado)
	                .isNotEmpty()
	                .hasSize(1)
	                .contains(instituicao);
	    }
	    @Test
	    public void deveObterInstituicaoPorID(){
	        //cenario
	        Long id = 1l;
	    	Instituicao instituicao = criarInstituicao();
	    	instituicao.setId(id);

	        Mockito.when(repository.findById(id)).thenReturn(Optional.of(instituicao));

	        //execucao
	        Optional<Instituicao> resultado = service.obterPorId(id);

	        //verificacao
	        Assertions.assertThat(resultado.isPresent()).isTrue();
	    }

	    @Test
	    public void deveRetornarVazioQuandoInstituicaoNaoExiste(){
	        //cenario
	        Long id = 1l;
	    	Instituicao instituicao = criarInstituicao();
	    	instituicao.setId(id);

	        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

	        //execucao
	        Optional<Instituicao> resultado = service.obterPorId(id);

	        //verificacao
	        Assertions.assertThat(resultado.isPresent()).isFalse();
	    }
	    @Test
	    public void deveLancarErroAoValidarInstituicao(){
	    	Instituicao instituicao = new Instituicao();
	    	Throwable erro  = Assertions.catchThrowable( ()-> service.validar(instituicao));
	        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Descrição inválida.");
	    	
	    }
	    public static Instituicao criarInstituicao(){
	    	return Instituicao.builder()
	    						.nome("FAESP")
	    						.rua("Pedro Gusso")
	    						.cep("10906887")
	    						.numero(123l)
	    						.build();
	    						
	    }
}
