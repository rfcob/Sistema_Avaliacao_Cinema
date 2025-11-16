package br.uel.SistemaAvaliacaoCinema.model;

import java.time.LocalDate;

public class Cliente {

    private Long idCliente;
    private String nome;
    private String cpf;
    private String email;
    private String cidade;
    private String estado;
    private String telefone;
    private LocalDate dataNascimento;
    private boolean ativo;

    // Campos de endereço comentados pois não estao no SQL (ARRUMAR!!!!!!!!!!!!!!!!)
    // private String rua;
    // private String bairro;
    // private String cep;
    // private String numero;

    public Cliente(){
    }

    // Getters
    public Long getIdCliente(){
        return idCliente;
    }

    public String getNome(){
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail(){
        return email;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getTelefone() {
        return telefone;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public boolean isAtivo() {
        return ativo;
    }


    //Setters
    public void setIdCliente(Long idCliente){
        this.idCliente = idCliente;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setCpf(String cpf){
        this.cpf = cpf;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setCidade(String cidade){
        this.cidade = cidade;
    }

    public void setEstado(String estado){
        this.estado = estado;
    }

    public void setTelefone(String telefone){
        this.telefone = telefone;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}