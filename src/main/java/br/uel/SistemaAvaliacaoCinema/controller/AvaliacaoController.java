package br.uel.SistemaAvaliacaoCinema.controller;

import br.uel.SistemaAvaliacaoCinema.service.AvaliacaoService;
import br.uel.SistemaAvaliacaoCinema.service.CinemaService;
import br.uel.SistemaAvaliacaoCinema.service.SessaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;
    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private SessaoService sessaoService;

    @GetMapping
    public String listarAvaliacoes(Model model,
                                   @RequestParam(value = "cinemaId", required = false) Long cinemaId,
                                   @RequestParam(value = "sessaoId", required = false) Long sessaoId,
                                   @RequestParam(value = "modo", required = false, defaultValue = "anonimas") String modo) {

        model.addAttribute("cinemas", cinemaService.listarCinemas());
        model.addAttribute("sessoes", sessaoService.listarSessoes(cinemaId, null, null));
        model.addAttribute("modo", modo);
        model.addAttribute("filtroCinemaId", cinemaId);
        model.addAttribute("filtroSessaoId", sessaoId);

        if (modo.equals("identificadas")) {
            model.addAttribute("avaliacoes", avaliacaoService.listarAvaliacoesIdentificadas(cinemaId, sessaoId));
        } else {
            model.addAttribute("avaliacoes", avaliacaoService.listarAvaliacoesAnonimas(cinemaId, sessaoId));
        }

        return "avaliacoes/lista_avaliacoes";
    }
}
