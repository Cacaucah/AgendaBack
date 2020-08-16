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
import br.com.agenda.educacional.AgendaEducacional.model.entity.Cliente;
import br.com.agenda.educacional.AgendaEducacional.model.repository.ClienteRepository;
import br.com.agenda.educacional.AgendaEducacional.services.impl.ClienteServiceImplementacao;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")

//as anotações acima indicam como o java irá rodar esse arquivo e com qual perfil, nesse
//caso será o perfil de tests informado em src/main/resources application-test.properties
//esse arquivo aponta para um banco em memória, o h2database
public class ClienteServiceTest {

	//com o spybean é possível realizar os testes utilizando os métodos reais da aplicação
		@SpyBean
	    ClienteServiceImplementacao service;

		//o mockbean é para mocar uma instancia a fazendo "fake"
	    @MockBean
	    ClienteRepository repository;
	    
	    @Test
	    public void deveSalvarInstituicao(){
	    	Cliente clienteASalvar = criarCliente();
	    	Mockito.doNothing().when(service).validar(clienteASalvar);
	    	Cliente clienteSalvo = criarCliente();
	    	clienteSalvo.setId(1l);
	    	clienteSalvo.setNome("FAESP");
	    	clienteSalvo.setRua("Pedro Gusso");
	    	clienteSalvo.setCep("10906887");
	    	clienteSalvo.setNumero(123l);
	    	
	    	//ao chamar o metodo salvar ele retorna o institutoSalvo
	    	Mockito.when(repository.save(clienteASalvar)).thenReturn(clienteSalvo);
	    	
	    	Cliente cliente = service.salvarCliente(clienteASalvar);
	    	
	    	//Assertions that quer dizer: verifique que
    		Assertions.assertThat(cliente.getId()).isEqualTo(clienteSalvo.getId());
    		Assertions.assertThat(cliente.getNome()).isEqualTo(clienteSalvo.getNome());
    		Assertions.assertThat(cliente.getRua()).isEqualTo(clienteSalvo.getRua());
    		Assertions.assertThat(cliente.getCep()).isEqualTo(clienteSalvo.getCep());
    		Assertions.assertThat(cliente.getNumero()).isEqualTo(clienteSalvo.getNumero());

	    }
	    @Test
	    public void naoDeveSalvarInstituicaoQuandoHouverErroDeValidacao(){
	        //cenario
	    	Cliente clienteASalvar = criarCliente();
	        Mockito.doThrow(RegraNegocioException.class).when(service).validar(clienteASalvar);

	        //execucacao verificacao
	        Assertions.catchThrowableOfType(()->service.salvarCliente(clienteASalvar), RegraNegocioException.class);

	        Mockito.verify(repository, Mockito.never()).save(clienteASalvar);
	    }
	    @Test
	    public void deveAtualizarUmaInstituicao(){
	    	Cliente clienteSalvo = criarCliente();
	    	clienteSalvo.setId(1l);
	    	clienteSalvo.setCep("015023698");
	        Mockito.doNothing().when(service).validar(clienteSalvo);
	        
	        Mockito.when(repository.save(clienteSalvo)).thenReturn(clienteSalvo);
	        
	        service.atualizarCliente(clienteSalvo);

	        Mockito.verify(repository, Mockito.times(1)).save(clienteSalvo);

	    }
	    @Test
	    public void deveLancarErroAoTentarAtualizarUmaInstituicaoNaoSalva(){
	    	Cliente clienteASalvar = criarCliente();
	        Assertions.catchThrowableOfType(()->service.atualizarCliente(clienteASalvar), NullPointerException.class);

	        Mockito.verify(repository, Mockito.never()).save(clienteASalvar);

	    }
	    @Test
	    public void deveDeletarInstituicao(){
	    	Cliente cliente = criarCliente();
	    	cliente.setId(1l);
	        service.deletar(cliente);

	        Mockito.verify(repository).delete(cliente);
	    }
	    @Test
	    public void deveLancarErroAoDeletarUmLancamentoQueNaoFoiSalvo(){
	    	Cliente cliente = criarCliente();
	        Assertions.catchThrowableOfType(()->service.deletar(cliente), NullPointerException.class);
	        
	        //verificacao
	        Mockito.verify(repository, Mockito.never()).delete(cliente);

	    }
	    @Test
	    public void deveFiltrarInsituicao(){
	        //cenario
	    	Cliente cliente = criarCliente();
	    	cliente.setId(1l);

	        List<Cliente> lista = Arrays.asList(cliente);
	        Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);

	        //execucacao
	        List<Cliente> resultado = service.buscar(cliente);

	        //verificacoes
	        Assertions.assertThat(resultado)
	                .isNotEmpty()
	                .hasSize(1)
	                .contains(cliente);
	    }
	    @Test
	    public void deveObterInstituicaoPorID(){
	        //cenario
	        Long id = 1l;
	    	Cliente cliente = criarCliente(); 
	    	cliente.setId(id);

	        Mockito.when(repository.findById(id)).thenReturn(Optional.of(cliente));

	        //execucao
	        Optional<Cliente> resultado = service.obterPorId(id);

	        //verificacao
	        Assertions.assertThat(resultado.isPresent()).isTrue();
	    }

	    @Test
	    public void deveRetornarVazioQuandoInstituicaoNaoExiste(){
	        //cenario
	        Long id = 1l;
	        Cliente cliente = criarCliente(); 
	        cliente.setId(id);

	        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

	        //execucao
	        Optional<Cliente> resultado = service.obterPorId(id);

	        //verificacao
	        Assertions.assertThat(resultado.isPresent()).isFalse();
	    }
	    @Test
	    public void deveLancarErroAoValidarInstituicao(){
	    	Cliente cliente = new Cliente();
	    	Throwable erro  = Assertions.catchThrowable( ()-> service.validar(cliente));
	        Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Descrição inválida.");
	    	
	    }
	    public static Cliente criarCliente(){
	    	return Cliente.builder()
	    						.nome("FAESP")
	    						.rua("Pedro Gusso")
	    						.cep("10906887")
	    						.numero(123l)
	    						.build();
	    						
	    }
}
