package br.uel.SistemaAvaliacaoCinema.model;


public class Cinema {

    private Long idCinema;
    private String nome;
    private String localizacao;
    private String tipoEstabelecimento;
    private Integer numeroSalas;

    private String resumoTiposSalas;

    // Construtor vazio
    public Cinema() {
    }

    // Getters

    public Long getIdCinema() {
        return idCinema;
    }

    public String getNome() {
        return nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public String getTipoEstabelecimento() {
        return tipoEstabelecimento;
    }

    public String getResumoTiposSalas() { return resumoTiposSalas; }


    //Settersm


    public Integer getNumeroSalas() {
        return numeroSalas;
    }

    public void setIdCinema(Long idCinema) {
        this.idCinema = idCinema;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public void setTipoEstabelecimento(String tipoEstabelecimento) {
        this.tipoEstabelecimento = tipoEstabelecimento;
    }

    public void setNumeroSalas(Integer numeroSalas) {
        this.numeroSalas = numeroSalas;
    }

    public void setResumoTiposSalas(String resumoTiposSalas) { this.resumoTiposSalas = resumoTiposSalas; }
}

