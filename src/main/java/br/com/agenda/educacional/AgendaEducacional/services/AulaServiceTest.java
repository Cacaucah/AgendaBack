/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
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

import br.com.agenda.educacional.AgendaEducacional.enums.NaturezaAula;
import br.com.agenda.educacional.AgendaEducacional.exceptions.RegraNegocioException;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Aula;
import br.com.agenda.educacional.AgendaEducacional.model.repository.AulaRepository;
import br.com.agenda.educacional.AgendaEducacional.services.impl.AulaServiceImplementacao;

/**
 * @author Ana Caroline
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AulaServiceTest {

	@SpyBean
	AulaServiceImplementacao service;

	// o mockbean é para mocar uma instancia a fazendo "fake"
	@MockBean
	AulaRepository repository;

	@Test
	public void deveSalvarAula() {
		LocalDate data = LocalDate.now();
		Aula aulaASalvar = criarAula();
		Mockito.doNothing().when(service).validar(aulaASalvar);
		Aula aulaSalva = criarAula();
		aulaSalva.setId(1l);
		aulaSalva.setData(data);
		aulaSalva.setHoraInicial(LocalTime.now());
		aulaSalva.setHoraFim(LocalTime.now());
		aulaSalva.setDetalhes("Noturna");
		aulaSalva.setTipoDeAula(NaturezaAula.PARTICULAR);
		aulaSalva.setValor(BigDecimal.valueOf(1));
		// ao chamar o metodo salvar ele retorna o institutoSalvo
		Mockito.when(repository.save(aulaASalvar)).thenReturn(aulaSalva);

		Aula aula = service.salvarAula(aulaASalvar);

		// Assertions that quer dizer: verifique que
		Assertions.assertThat(aula.getId()).isEqualTo(aulaSalva.getId());
		Assertions.assertThat(aula.getAluno()).isEqualTo(aulaSalva.getAluno());
		Assertions.assertThat(aula.getProfessor()).isEqualTo(aulaSalva.getProfessor());
		Assertions.assertThat(aula.getInstituicao()).isEqualTo(aulaSalva.getInstituicao());
		Assertions.assertThat(aula.getMateria()).isEqualTo(aulaSalva.getMateria());
		Assertions.assertThat(aula.getData()).isEqualTo(aulaSalva.getData());
		Assertions.assertThat(aula.getHoraInicial()).isEqualTo(aulaSalva.getHoraInicial());
		Assertions.assertThat(aula.getHoraFim()).isEqualTo(aulaSalva.getHoraFim());
		Assertions.assertThat(aula.getTipoDeAula()).isEqualTo(aulaSalva.getTipoDeAula());
		Assertions.assertThat(aula.getValor()).isEqualTo(aulaSalva.getValor());
		Assertions.assertThat(aula.getDetalhes()).isEqualTo(aulaSalva.getDetalhes());
	}

	@Test
	public void naoDeveSalvarUmaAulaQuandoHouverErroDeValidacao() {
		// cenario
		Aula aulaASalvar = criarAula();
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(aulaASalvar);

		// execucacao verificacao
		Assertions.catchThrowableOfType(() -> service.salvarAula(aulaASalvar), RegraNegocioException.class);

		Mockito.verify(repository, Mockito.never()).save(aulaASalvar);
	}

	@Test
	public void deveAtualizarAula() {
		// cenario
		Aula aulaSalva = criarAula();
		aulaSalva.setId(1l);
		aulaSalva.setDetalhes("AH");
		Mockito.doNothing().when(service).validar(aulaSalva);

		Mockito.when(repository.save(aulaSalva)).thenReturn(aulaSalva);

		// execucao
		service.atualizarAula(aulaSalva);

		// verifique que o repository chamou o método save ao menos uma vez com
		// o lancamentoSalvo
		Mockito.verify(repository, Mockito.times(1)).save(aulaSalva);
	}

	@Test
	public void deveLancarErroAoTentarAtualizarAulaQueAindaNaoFoiSalva() {
		// cenario
		Aula aulaASalvar = criarAula();

		// execucacao verificacao
		Assertions.catchThrowableOfType(() -> service.atualizarAula(aulaASalvar), NullPointerException.class);

		Mockito.verify(repository, Mockito.never()).save(aulaASalvar);
	}

	@Test
	public void deveFiltrarAulas() {
		// cenario
		Aula aula = criarAula();
		aula.setId(1l);

		List<Aula> lista = Arrays.asList(aula);
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);

		// execucacao
		List<Aula> resultado = service.buscar(aula);

		// verificacoes
		Assertions.assertThat(resultado).isNotEmpty().hasSize(1).contains(aula);
	}

	@Test
	public void deveDeletarUmaAula() {
		// cenario
		LocalDate data = LocalDate.now();
		Aula aula = criarAula();
		aula.setId(1l);
		aula.setData(data);
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(aula);

		service.deletar(aula);

		// verificacao
		Mockito.verify(repository).delete(aula);
	}

	@Test
	public void deveObterAulaPorID() {
		// cenario
		Long id = 1l;
		Aula aula = criarAula();
		aula.setId(id);

		Mockito.when(repository.findById(id)).thenReturn(Optional.of(aula));

		// execucao
		Optional<Aula> resultado = service.obterPorId(id);

		// verificacao
		Assertions.assertThat(resultado.isPresent()).isTrue();
	}

	@Test
	public void deveRetornarVazioQuandoAulaNaoExiste() {
		// cenario
		Long id = 1l;
		Aula aula = criarAula();
		aula.setId(id);

		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

		// execucao
		Optional<Aula> resultado = service.obterPorId(id);

		// verificacao
		Assertions.assertThat(resultado.isPresent()).isFalse();
	}

	@Test
	public void deveLancarErrosAoValidarUmLancamento() {
		Aula aula = new Aula();

		Throwable erro = Assertions.catchThrowable(() -> service.validar(aula));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Descrição inválida.");

		aula.setTipoDeAula(NaturezaAula.PARTICULAR);
		aula.setValor(BigDecimal.ZERO);
		aula.setId(1l);
		aula.setDetalhes("Hey");

		erro = Assertions.catchThrowable(() -> service.validar(aula));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido.");
		
		LocalDate data = LocalDate.now();
		aula.setData(data);
		aula.setTipoDeAula(NaturezaAula.PARTICULAR);
		aula.setValor(BigDecimal.TEN);
		aula.setId(1l);
		aula.setDetalhes("Hey");
		erro = Assertions.catchThrowable(() -> service.validar(aula));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Não é possível editar uma aula antiga do histórico.");
	}

	private Aula criarAula() {
		// TODO Auto-generated method stub
		LocalDate data = LocalDate.now();
		return Aula.builder().data(data).horaInicial(LocalTime.now())
				.detalhes("Noturna").tipoDeAula(NaturezaAula.PARTICULAR).valor(BigDecimal.valueOf(1)).build();
	}
}
