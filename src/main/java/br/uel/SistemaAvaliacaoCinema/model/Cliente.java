package br.uel.SistemaAvaliacaoCinema.model;

import java.time.LocalDate;

public class Cliente {

    private Long idCliente;
    private String nome;
    private String cpf;
    private String email;
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String numero;
    private String telefone;
    private LocalDate dataNascimento;
    private boolean ativo;

    public Cliente(){
    }

    // Getters
    public Long getIdCliente(){
        return idCliente;
    }

    public String getNomeCliente(){
        return nome;
    }

    public String getCpfCliente() {
        return cpf;
    }

    public String getEmailCliente(){
        return email;
    }

    public String getRuaCliente() {
        return rua;
    }

    public String getBairroCliente() {
        return bairro;
    }
    public String getCidadeCliente() {
        return cidade;
    }

    public String getEstadoCliente() {
        return estado;
    }

    public String getCepCliente() {
        return cep;
    }

    public String getTelefoneCliente() {
        return telefone;
    }

    public String getNumeroCasaCliente() {
        return numero;
    }

    public LocalDate getDataNascimentoCliente() {
        return dataNascimento;
    }

    public boolean isAtivoCliente() {
        return ativo;
    }


    //Setters
    public void setIdcliente(Long idCliente){
        this.idCliente = idCliente;
    }

    public void setNomeCliente(String nome){
        this.nome = nome;
    }

    public void setCpfCliente(String cpf){
        this.cpf = cpf;
    }

    public void setEmailCliente(String email){this.email = email;}

    public void setRuaCliente(String rua){
        this.rua = rua;
    }

    public  void setBairroCliente(String bairro){
        this.bairro = bairro;
    }

    public void setCidadeCliente(String cidade){
        this.cidade = cidade;
    }

    public void setEstadoCliente(String estado){
        this.estado = estado;
    }

    public void setCepCliente(String cep){
        this.cep = cep;
    }

    public void setNumeroCasaCliente(String numero){
        this.numero = numero;
    }

    public void setTelefone(String telefone){
        this.telefone = telefone;
    }

    public void setDataNascimentoCliente(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setAtivoCliente(boolean ativo) {
        this.ativo = ativo;
    }
}