package br.uel.SistemaAvaliacaoCinema.model;

public class Criterio {
    private Long idCriterio;
    private String nomeCriterio;
    private String descricao;

    // Getters
    public Long getIdCriterio() {
        return idCriterio;
    }

    public String getNomeCriterio() {
        return nomeCriterio;
    }

    public String getDescricao() {
        return descricao;
    }

    //Setters

    public void setIdCriterio(Long idCriterio) {
        this.idCriterio = idCriterio;
    }
    public void setNomeCriterio(String nomeCriterio) {
    this.nomeCriterio = nomeCriterio;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}