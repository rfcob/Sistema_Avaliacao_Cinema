package br.uel.SistemaAvaliacaoCinema.form;

import java.util.ArrayList;
import java.util.List;

public class QuestionarioForm {

    private Long idCliente;
    private Long idSessao;
    private String comentario;


    private boolean comentarioAnonimo = false;


    private List<RespostaCriterioForm> respostas;

    public QuestionarioForm() {
        this.respostas = new ArrayList<>();
    }

    // Getters
    public Long getIdCliente() { return idCliente; }
    public Long getIdSessao() { return idSessao; }
    public String getComentario() { return comentario; }
    public List<RespostaCriterioForm> getRespostas() { return respostas; }
    public boolean isComentarioAnonimo() { return comentarioAnonimo;
    }

    //Setters
    public void setRespostas(List<RespostaCriterioForm> respostas) { this.respostas = respostas; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public void setIdSessao(Long idSessao) { this.idSessao = idSessao; }
    public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

    public void setComentarioAnonimo(boolean comentarioAnonimo) {
        this.comentarioAnonimo = comentarioAnonimo;
    }


}