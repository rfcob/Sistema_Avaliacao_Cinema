package br.uel.SistemaAvaliacaoCinema.model;

public class Cliente {

    // Atributos
    private Long idCliente;   //chave primaria
    private String nome;
    private String cpf;
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String numeroCasa;
    private String telefone;

    //contrutor vazio
    public Cliente(){

    }

    // Getters
    public Long getId_cliente(){
        return idCliente;
    }

    public String getNome(){
        return nome;
    }

    public String getCpf_Cliente() {
        return cpf;
    }

    public String getRua_Cliente() {
        return rua;
    }

    public String getBairro_Cliente() {
        return bairro;
    }
    public String getCidade_Cliente() {
        return cidade;
    }

    public String getEstado_Cliente() {
        return estado;
    }

    public String getCep_Cliente() {
        return cep;
    }

    public String getTelefone_Cliente() {
        return telefone;
    }

    public String getNumeroCasa_Cliente() {
        return numeroCasa;
    }


    //Setters

    public void setId_cliente(Long idCliente){
        this.idCliente = idCliente;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setCpf_Cliente(String cpf){
        this.cpf = cpf;
    }

    public void setRua_Cliente(String rua){
        this.rua = rua;
    }

    public  void setBairro_Cliente(String bairro){
        this.bairro = bairro;
    }

    public void setCidade_Cliente(String cidade){
        this.cidade = cidade;
    }

    public void setEstado_Cliente(String estado){
        this.estado = estado;
    }

    public void setCep_Cliente(String cep){
        this.cep = cep;
    }

    public void setNumeroCasa_Cliente(String numeroCasa){
        this.numeroCasa = numeroCasa;
    }

    public void setTelefone(String telefone){
        this.telefone = telefone;
    }


}

