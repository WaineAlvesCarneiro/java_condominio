package com.wa.java_condominio.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.wa.java_condominio.model.Imovel;
import com.wa.java_condominio.model.Morador;
import com.wa.java_condominio.utils.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MoradorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "Nome é obrigatório.")
    private String nome;

    @NotBlank(message = "Celular é obrigatório.")
    private String celular;

    @NotBlank(message = "E-mail é obrigatório.")
    private String email;

    @NotNull(message = "Proprietário é obrigatório.")
    private Boolean isProprietario;

    @NotNull(message = "Data de entrada é obrigatória.")
    @JsonDeserialize(using = LocalDateToLocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dataEntrada;

    @JsonDeserialize(using = LocalDateToLocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dataSaida;

    @NotNull(message = "Data de inclusão é obrigatória.")
    @JsonDeserialize(using = LocalDateToLocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dataInclusao;

    @JsonDeserialize(using = LocalDateToLocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime dataAlteracao;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ImovelDTO imovelDto;

    @NotNull(message = "Imóvel é obrigatório.")
    private Long imovelId;

    public MoradorDTO() {
    }

    public MoradorDTO(Long id, String nome, String celular, String email, Boolean isProprietario,
                      LocalDateTime dataEntrada, LocalDateTime dataSaida,
                      LocalDateTime dataInclusao, LocalDateTime dataAlteracao) {
        this.id = id;
        this.nome = nome;
        this.celular = celular;
        this.email = email;
        this.isProprietario = isProprietario;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.dataInclusao = dataInclusao;
        this.dataAlteracao = dataAlteracao;
    }

    public MoradorDTO(Morador morador) {
        this.id = morador.getId();
        this.nome = morador.getNome();
        this.celular = morador.getCelular();
        this.email = morador.getEmail();
        this.isProprietario = morador.getIsProprietario();

        this.dataEntrada = DateTimeUtils.fromUTC(morador.getDataEntrada());
        this.dataSaida = DateTimeUtils.fromUTC(morador.getDataSaida());
        this.dataInclusao = DateTimeUtils.fromUTC(morador.getDataInclusao());
        this.dataAlteracao = DateTimeUtils.fromUTC(morador.getDataAlteracao());
    }

    public MoradorDTO(Morador morador, Imovel imovel) {
        this(morador);
        this.imovelDto = new ImovelDTO(imovel);
        this.imovelId = imovel.getId();
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

	public ImovelDTO getImovelDto() {
		return imovelDto;
	}

	public void setImovelDto(ImovelDTO imovelDto) {
		this.imovelDto = imovelDto;
	}
	
	public Long getImovelId() {
	    return imovelId;
	}

	public void setImovelId(Long imovelId) {
	    this.imovelId = imovelId;
	}

}
