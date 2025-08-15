package com.wa.java_condominio.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "morador")
public class Morador implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String celular;
	private String email;
	private Boolean isProprietario;
	
	@Column(name = "data_entrada", columnDefinition = "TIMESTAMP")
	private LocalDateTime dataEntrada;
	
	@Column(name = "data_saida", columnDefinition = "TIMESTAMP")
	private LocalDateTime dataSaida;
	
	@CreationTimestamp
	@Column(name = "data_inclusao", columnDefinition = "TIMESTAMP", nullable = false)
	private LocalDateTime dataInclusao;
	
	@Column(name = "data_alteracao", columnDefinition = "TIMESTAMP")
	private LocalDateTime dataAlteracao;
	
	@PreUpdate
	public void preUpdate() {
		this.dataAlteracao = LocalDateTime.now();
	}
	
	@ManyToOne
	@JoinColumn(name = "imovel_id", nullable = false)
	private Imovel imovel;

	public Morador() {
	}

	public Morador(Long id, String nome, String celular, String email, Boolean isProprietario,
			LocalDateTime dataEntrada, LocalDateTime dataSaida, LocalDateTime dataAlteracao) {
		this.id = id;
		this.nome = nome;
		this.celular = celular;
		this.email = email;
		this.isProprietario = isProprietario;
		this.dataEntrada = dataEntrada;
		this.dataSaida = dataSaida;
		this.dataAlteracao = dataAlteracao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getIsProprietario() {
		return isProprietario;
	}

	public void setIsProprietario(Boolean isProprietario) {
		this.isProprietario = isProprietario;
	}

	public LocalDateTime getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(LocalDateTime dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public LocalDateTime getDataSaida() {
		return dataSaida;
	}

	public void setDataSaida(LocalDateTime dataSaida) {
		this.dataSaida = dataSaida;
	}

	public LocalDateTime getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(LocalDateTime dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public LocalDateTime getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(LocalDateTime dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public Imovel getImovel() {
		return imovel;
	}

	public void setImovel(Imovel imovel) {
		this.imovel = imovel;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Morador other = (Morador) obj;
		return Objects.equals(id, other.id);
	}
}