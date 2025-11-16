package br.uel.SistemaAvaliacaoCinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    //raiz
    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/dashboard";
    }

    //página inicial
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