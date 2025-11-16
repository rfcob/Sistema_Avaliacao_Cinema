package br.uel.SistemaAvaliacaoCinema.form;

import br.uel.SistemaAvaliacaoCinema.model.Criterio;
import java.math.BigDecimal;

public class RespostaCriterioForm {
    private Criterio criterio;
    private BigDecimal nota;

    // Construtor
    public RespostaCriterioForm(Criterio criterio) {

        this.criterio = criterio;
    }

    // Construtor vazio
    public RespostaCriterioForm() {

    }

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