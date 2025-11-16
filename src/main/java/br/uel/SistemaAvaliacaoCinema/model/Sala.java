package br.uel.SistemaAvaliacaoCinema.model;

public class Sala {

    private Long idSala; // PK
    private Integer capacidade;
    private String tipoSom;
    private String formatoExibicao;
    private String tecnologia;
    private String tipoSala;
    private boolean ativo = true;
    private Long idCinema;// FK

    // Campo extra (via JOIN)
    private String nomeCinema;


    // Getters


    public Long getIdSala() {
        return idSala;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public String getTipoSom() {
        return tipoSom;
    }

    public String getFormatoExibicao() {
        return formatoExibicao;
    }

    public String getTecnologia() {
        return tecnologia;
    }

    public String getTipoSala() {
        return tipoSala;
    }

    public Long getIdCinema() {
        return idCinema;
    }

    public String getNomeCinema() {
        return nomeCinema;
    }

    public boolean isAtivo() {
        return ativo;
    }

    //Setters

    public void setIdSala(Long idSala) {
        this.idSala = idSala;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public void setTipoSom(String tipoSom) {
        this.tipoSom = tipoSom;
    }

    public void setFormatoExibicao(String formatoExibicao) {
        this.formatoExibicao = formatoExibicao;
    }

    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }

    public void setTipoSala(String tipoSala) {
        this.tipoSala = tipoSala;
    }

    public void setIdCinema(Long idCinema) {
        this.idCinema = idCinema;
    }

    public void setNomeCinema(String nomeCinema) {
        this.nomeCinema = nomeCinema;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

}