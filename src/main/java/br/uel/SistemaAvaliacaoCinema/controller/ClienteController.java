package br.uel.SistemaAvaliacaoCinema.controller;

import br.uel.SistemaAvaliacaoCinema.model.Cliente;
import br.uel.SistemaAvaliacaoCinema.service.ClienteService;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String cliente(){
        return "cliente";
    }

    @GetMapping("/novo")
    public String cadastrarNovoCliente(Model model){
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("TítuloPágina", "Cadastrar Novo Cliente");
        return "cliente/form_Cliente";
    }

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("cinemas", clienteService.listarClientes());
        return "cliente/lista_Cliente";
    }

}
