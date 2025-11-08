package br.uel.SistemaAvaliacaoCinema.model;

/**
 * Representa a entidade Cinema.
 * Não contém anotações JPA.
 */

public class Cinema {

    // Nomes dos campos em Java
    private Long idCinema;
    private String nome;
    private String localizacao;
    private String tipoEstabelecimento;
    private Integer numeroSalas;

    // Construtor vazio
    public Cinema() {
    }

    // Getters e Setters

    public Long getIdCinema() {
        return idCinema;
    }

    public void setIdCinema(Long idCinema) {
        this.idCinema = idCinema;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getTipoEstabelecimento() {
        return tipoEstabelecimento;
    }

    public void setTipoEstabelecimento(String tipoEstabelecimento) {
        this.tipoEstabelecimento = tipoEstabelecimento;
    }

    public Integer getNumeroSalas() {
        return numeroSalas;
    }

    public void setNumeroSalas(Integer numeroSalas) {
        this.numeroSalas = numeroSalas;
    }
}