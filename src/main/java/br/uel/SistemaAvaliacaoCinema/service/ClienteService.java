package br.uel.SistemaAvaliacaoCinema.service;

import br.uel.SistemaAvaliacaoCinema.model.Cliente;
import br.uel.SistemaAvaliacaoCinema.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> listarClientes() {
        return clienteRepository.listarTodos();
    }

    public Cliente buscarClientePorId(Long id) {
        return clienteRepository.encontrePorId(id)
                .orElseThrow(() -> new RuntimeException("Cliente não cadastrado com id: " + id));
    }

    public Cliente buscarClientePorCpf(String cpf) {
        return clienteRepository.encontrePorCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Cliente não cadastrado com CPF: " + cpf));
    }

    public Cliente buscarClientePorEmail(String email) {
        return clienteRepository.encontrePorEmail(email)
                .orElseThrow(() -> new RuntimeException("Cliente não cadastrado com email: " + email));
    }


    public List<Cliente> pesquisarClientesPorNome(String nome) {
        return clienteRepository.encontrePorNomeContendo(nome);
    }

    public void cadastrarCliente(Cliente cliente) {
        cliente.setAtivo(true);
        clienteRepository.incluir(cliente);
    }

    public void atualizarCliente(Long id, Cliente cliente) {
        cliente.setIdCliente(id);
        cliente.setAtivo(true);

        clienteRepository.update(cliente);
    }

    public void deletarCliente(Long id) {
        clienteRepository.softDeletePorId(id);
    }

    // PAra busca
    public List<Cliente> listarClientes(String termo) {
        if (termo != null && !termo.isBlank()) {
            return clienteRepository.buscarPorTermo(termo);
        }
        // Se não tiver termo, lista todos
        return clienteRepository.listarTodos();
    }
}