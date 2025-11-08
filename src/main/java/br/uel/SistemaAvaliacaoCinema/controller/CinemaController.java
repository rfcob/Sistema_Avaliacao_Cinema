package br.uel.SistemaAvaliacaoCinema.controller;

import br.uel.SistemaAvaliacaoCinema.model.Cinema;
import br.uel.SistemaAvaliacaoCinema.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cinema")

public class CinemaController {

    @Autowired
    private CinemaService cinemaService;

    @GetMapping("/novo")
    public String cadastrarNovoCinema(Model model){
        model.addAttribute("cinema", new Cinema());
        model.addAttribute("TítuloPágina", "Cadastrar Novo Cinema");
        return "cinema/form_Cinema";
    }

    @PostMapping
    public String salvarCinema(Cinema cinema, RedirectAttributes ra) {
        try {
            cinemaService.cadastrarCinema(cinema);
            ra.addFlashAttribute("success", "Cinema cadastrado com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erro ao cadastrar cinema: " + e.getMessage());
        }
        return "redirect:/cinema";
    }




}