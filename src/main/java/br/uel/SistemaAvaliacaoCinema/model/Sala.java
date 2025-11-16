package br.uel.SistemaAvaliacaoCinema.model;

public class Sala {

    private Long numSala; // Chave Primária
    private Integer capacidade;
    private String tipoSom;
    private String formatoExibicao;
    private String tecnologia;
    private String tipoSala;
    private boolean ativo = true;

    // Chave Estrangeira
    private Long idCinema;

    // Campo extra (não vem da tabela Sala) para armazenar o nome do Cinema (via JOIN)
    private String nomeCinema;


    // Getters


    public Long getNumSala() {
        return numSala;
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


    public void setNumSala(Long numSala) {
        this.numSala = numSala;
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