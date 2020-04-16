package br.com.agenda.educacional.AgendaEducacional.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//mapeamento de entidades
@Entity
@Table(name = "professor", schema = "agendaeducacional")
@Builder
@Data
//cria um construtor sem argumentos
@NoArgsConstructor
//cria um construtor com argumentos
@AllArgsConstructor
public class Professor {

	//mapeamento da coluna id do banco de dados
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//identificando a coluna
	@Column(name="id")
	//dizendo ao java que o valor é um Long
	private Long id;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "senha")
	@JsonIgnore
	private String senha;
}

