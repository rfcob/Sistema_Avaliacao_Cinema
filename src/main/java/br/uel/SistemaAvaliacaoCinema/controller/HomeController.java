package br.uel.SistemaAvaliacaoCinema.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para lidar com rotas de navegação globais,
 * como a página inicial.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String paginaInicial() {
        // Redireciona para a tua lista de filmes
        return "redirect:/cinema";
    }

    // (Opcional) Podes adicionar um /index também, se quiseres
    @GetMapping("/index")
    public String paginaIndex() {
        return "redirect:/cinema";
    }
}