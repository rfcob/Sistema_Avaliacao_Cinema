package br.uel.SistemaAvaliacaoCinema.controller;

import br.uel.SistemaAvaliacaoCinema.model.Cliente;
import br.uel.SistemaAvaliacaoCinema.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.listarClientes());
        return "clientes/lista_clientes";
    }

    @GetMapping("/novo")
    public String mostrarFormularioNovo(Model model) {
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("tituloPagina", "Cadastrar Novo Cliente");
        return "clientes/form_clientes";
    }

    @PostMapping
    public String salvarCliente(Cliente cliente, RedirectAttributes ra) {
        try {
            clienteService.cadastrarCliente(cliente);
            ra.addFlashAttribute("success", "Cliente cadastrado com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erro ao cadastrar cliente: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        try {
            Cliente cliente = clienteService.buscarClientePorId(id);
            model.addAttribute("cliente", cliente);
            model.addAttribute("tituloPagina", "Editar Cliente: " + cliente.getNomeCliente());
            return "clientes/form_clientes";
        } catch (RuntimeException e) {
            return "redirect:/clientes";
        }
    }


    @PutMapping("/{id}")
    public String atualizarCliente(@PathVariable Long id, Cliente cliente, RedirectAttributes ra) {
        try {
            clienteService.atualizarCliente(id, cliente);
            ra.addFlashAttribute("success", "Cliente atualizado com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erro ao atualizar cliente: " + e.getMessage());
        }
        return "redirect:/clientes";
    }

    @DeleteMapping("/{id}")
    public String deletarCliente(@PathVariable Long id, RedirectAttributes ra) {
        try {
            clienteService.deletarCliente(id);
            ra.addFlashAttribute("success", "Cliente excluído com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erro ao excluir cliente: " + e.getMessage());
        }
        return "redirect:/clientes";
    }
}
