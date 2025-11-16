package br.uel.SistemaAvaliacaoCinema.model;

public class Avaliacao {
    private Long id;
    private Long sessaoId;
    private String sala;
    private String filme;
    private int nota;
    private String comentario;
    private boolean anonima;
    private String cliente;

    // Getters
    public Long getId() {
        return id;
    }

    public Long getSessaoId() {
        return sessaoId;
    }

    public String getSala() {
        return sala;
    }

    public String getFilme() {
        return filme;
    }

    public int getNota() {
        return nota;
    }

    public String getComentario() {
        return comentario;
    }

    public boolean isAnonima() {
        return anonima;
    }

    public String getCliente() {
        return cliente;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setSessaoId(Long sessaoId) {
        this.sessaoId = sessaoId;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public void setFilme(String filme) {
        this.filme = filme;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setAnonima(boolean anonima) {
        this.anonima = anonima;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }
}
