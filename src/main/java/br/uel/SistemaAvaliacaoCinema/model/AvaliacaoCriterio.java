package br.uel.SistemaAvaliacaoCinema.model;

import java.math.BigDecimal;

public class AvaliacaoCriterio {


    private Criterio criterio;
    private BigDecimal nota; // Nota de 0 a 5

    // Construtor
    public AvaliacaoCriterio(Criterio criterio) {

        this.criterio = criterio;
    }

    // Construtor vazio
    public AvaliacaoCriterio() {}

    // Getters
    public Criterio getCriterio() {
        return criterio;
    }

    public BigDecimal getNota() {
        return nota;
    }

    //Setters

    public void setCriterio(Criterio criterio) {
        this.criterio = criterio;
    }
    public void setNota(BigDecimal nota) {
        this.nota = nota;
    }
}