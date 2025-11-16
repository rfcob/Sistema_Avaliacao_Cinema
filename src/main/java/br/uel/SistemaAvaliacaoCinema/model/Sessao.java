package br.uel.SistemaAvaliacaoCinema.model;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class Sessao {

    private Long idSessao; // PK
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Double preco;
    private LocalTime hora;
    private String idioma;
    private Long idSala;   // FK
    private Long idFilme;  // FK
    private boolean ativo = true;

    // Campos extras para JOIN
    private String nomeSala;
    private String tituloFilme;

    // Getters e Setters
    public Long getIdSessao() { return idSessao; }
    public void setIdSessao(Long idSessao) { this.idSessao = idSessao; }

    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }

    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }

    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public Long getIdSala() { return idSala; }
    public void setIdSala(Long idSala) { this.idSala = idSala; }

    public Long getIdFilme() { return idFilme; }
    public void setIdFilme(Long idFilme) { this.idFilme = idFilme; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    public String getNomeSala() { return nomeSala; }
    public void setNomeSala(String nomeSala) { this.nomeSala = nomeSala; }

    public String getTituloFilme() { return tituloFilme; }
    public void setTituloFilme(String tituloFilme) { this.tituloFilme = tituloFilme; }
}
