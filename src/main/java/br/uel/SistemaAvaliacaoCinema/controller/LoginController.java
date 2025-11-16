package br.uel.SistemaAvaliacaoCinema.controller;

import br.uel.SistemaAvaliacaoCinema.model.Cliente;
import br.uel.SistemaAvaliacaoCinema.model.Sessao; // Importe o modelo Sessao
import br.uel.SistemaAvaliacaoCinema.service.ClienteService;
import br.uel.SistemaAvaliacaoCinema.service.SessaoService; // Importe o serviço Sessao
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // Importe o Model
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private SessaoService sessaoService;

    @GetMapping("/login")
    public String exibirPaginaLogin(Model model) {
        List<Sessao> sessoes = sessaoService.listarSessoes(null, null, null);

        model.addAttribute("sessoes", sessoes);

        return "login";
    }

    @PostMapping("/login/redirecionar")
    public String redirecionarParaQuestionario(
            @RequestParam("email") String email,
            @RequestParam("sessaoId") Long sessaoId,
            RedirectAttributes ra) {

        try {
            Cliente cliente = clienteService.buscarClientePorEmail(email);
            Long clienteId = cliente.getIdCliente();

            return String.format("redirect:/questionario?cliente=%d&sessao=%d", clienteId, sessaoId);

        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", "Email não encontrado no banco de dados.");
            return "redirect:/login";
        }
    }
}