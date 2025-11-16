package br.uel.SistemaAvaliacaoCinema.model;

public class Filme {

    private Long idFilme;
    private String titulo;
    private Integer duracao;
    private String genero;
    private String elenco;
    private String direcao;
    private Integer anoProducao;

    // Getters


    public Long getIdFilme() {
        return idFilme;
    }

    public String getTitulo() {
        return titulo;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public String getGenero() {
        return genero;
    }

    public String getElenco() {
        return elenco;
    }

    public String getDirecao() {
        return direcao;
    }


    public Integer getAnoProducao() {
        return anoProducao;
    }

    //Setters


    public void setIdFilme(Long idFilme) {
        this.idFilme = idFilme;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setElenco(String elenco) {
        this.elenco = elenco;
    }

    public void setDirecao(String direcao) {
        this.direcao = direcao;
    }


    public void setAnoProducao(Integer anoProducao) {
        this.anoProducao = anoProducao;
    }

}