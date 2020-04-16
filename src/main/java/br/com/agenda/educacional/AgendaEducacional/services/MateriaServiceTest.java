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

import br.com.agenda.educacional.AgendaEducacional.exceptions.RegraNegocioException;
import br.com.agenda.educacional.AgendaEducacional.model.entity.Materia;
import br.com.agenda.educacional.AgendaEducacional.model.repository.MateriaRepository;
import br.com.agenda.educacional.AgendaEducacional.services.impl.MateriaServiceImplementacao;

/**
 * @author Ana Caroline
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class MateriaServiceTest {

	// com o spybean é possível realizar os testes utilizando os métodos reais
	// da aplicação

	@SpyBean
	MateriaServiceImplementacao service;

	// o mockbean é para mocar uma instancia a fazendo "fake"
	@MockBean
	MateriaRepository repository;

	@Test
	public void deveSalvarMateria() {
		Materia materiaASalvar = criarMateria();
		Mockito.doNothing().when(service).validar(materiaASalvar);
		Materia materiaSalva = criarMateria();
		materiaSalva.setId(1l);
		materiaSalva.setNome("Matematica");

		// ao chamar o metodo salvar ele retorna o institutoSalvo
		Mockito.when(repository.save(materiaASalvar)).thenReturn(materiaSalva);

		Materia materia = service.salvarMateria(materiaASalvar);

		// Assertions that quer dizer: verifique que
		Assertions.assertThat(materia.getId()).isEqualTo(materiaSalva.getId());
		Assertions.assertThat(materia.getNome()).isEqualTo(materiaSalva.getNome());

	}

	@Test
	public void naoDeveSalvarMateriaQuandoHouverErroDeValidacao() {
		// cenario
		Materia materiaASalvar = criarMateria();
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(materiaASalvar);

		// execucacao verificacao
		Assertions.catchThrowableOfType(() -> service.salvarMateria(materiaASalvar), RegraNegocioException.class);

		Mockito.verify(repository, Mockito.never()).save(materiaASalvar);
	}

	@Test
	public void deveAtualizarUmaMateria() {
		Materia materiaSalva = criarMateria();
		materiaSalva.setId(1l);
		materiaSalva.setNome("matematica");
		Mockito.doNothing().when(service).validar(materiaSalva);

		Mockito.when(repository.save(materiaSalva)).thenReturn(materiaSalva);

		service.atualizarMateria(materiaSalva);

		Mockito.verify(repository, Mockito.times(1)).save(materiaSalva);

	}

	@Test
	public void deveLancarErroAoTentarAtualizarUmaMateriaNaoSalva() {
		Materia materiaASalvar = criarMateria();
		Assertions.catchThrowableOfType(() -> service.atualizarMateria(materiaASalvar), NullPointerException.class);

		Mockito.verify(repository, Mockito.never()).save(materiaASalvar);

	}

	@Test
	public void deveDeletarMateria() {
		Materia materia = criarMateria();
		materia.setId(1l);
		service.deletarMateria(materia);

		Mockito.verify(repository).delete(materia);
	}

	@Test
	public void deveLancarErroAoDeletarMateriaQueNaoFoiSalva() {
		Materia materia = criarMateria();
		Assertions.catchThrowableOfType(() -> service.deletarMateria(materia), NullPointerException.class);

		// verificacao
		Mockito.verify(repository, Mockito.never()).delete(materia);

	}

	@Test
	public void deveFiltrarMateria() {
		// cenario
		Materia materia = criarMateria();
		materia.setId(1l);

		List<Materia> lista = Arrays.asList(materia);
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);

		// execucacao
		List<Materia> resultado = service.buscarMaterias(materia);

		// verificacoes
		Assertions.assertThat(resultado).isNotEmpty().hasSize(1).contains(materia);
	}

	@Test
	public void deveObterMateriaPorID() {
		// cenario
		Long id = 1l;
		Materia materia = criarMateria();
		materia.setId(id);

		Mockito.when(repository.findById(id)).thenReturn(Optional.of(materia));

		// execucao
		Optional<Materia> resultado = service.obterPorId(id);

		// verificacao
		Assertions.assertThat(resultado.isPresent()).isTrue();
	}

	@Test
	public void deveRetornarVazioQuandoMateriaNaoExiste() {
		// cenario
		Long id = 1l;
		Materia materia = criarMateria();
		materia.setId(id);

		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

		// execucao
		Optional<Materia> resultado = service.obterPorId(id);

		// verificacao
		Assertions.assertThat(resultado.isPresent()).isFalse();
	}

	@Test
	public void deveLancarErroAoValidarMateria() {
		Materia materia = new Materia();
		Throwable erro = Assertions.catchThrowable(() -> service.validar(materia));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Descrição inválida.");

	}

	public static Materia criarMateria() {
		return Materia.builder().nome("Matematica").build();

	}
}
