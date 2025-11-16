package br.uel.SistemaAvaliacaoCinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para lidar com rotas de navegação globais,
 * como a página inicial e o dashboard principal.
 */
@Controller
public class HomeController {

    /**
     * Redireciona a rota raiz (/) para o dashboard.
     */
    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/dashboard";
    }

    /**
     * Mostra a página principal do dashboard.
     * Procura o ficheiro: templates/dashboard.html
     */
    @GetMapping("/dashboard")
    public String dashboardPage() {
        return "dashboard";
    }

    // (O /index agora é opcional, mas podemos mantê-lo a redirecionar)
    @GetMapping("/index")
    public String paginaIndex() {
        return "redirect:/dashboard";
    }
}