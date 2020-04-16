/**
 * 
 */
package br.com.agenda.educacional.AgendaEducacional.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ana Caroline
 *
 */
@Entity
@Table(name = "instituicao", schema = "agendaeducacional")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instituicao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	//dizendo ao java que o valor é um Long
	private Long id;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "rua")
	private String rua;
	
	@Column(name = "numero")
	private Long numero;
	
	@Column(name = "cep")
	private String cep;
}
