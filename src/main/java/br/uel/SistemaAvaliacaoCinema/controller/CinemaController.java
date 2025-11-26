package br.uel.SistemaAvaliacaoCinema.controller;

import br.uel.SistemaAvaliacaoCinema.model.Cinema;
import br.uel.SistemaAvaliacaoCinema.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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

//    @GetMapping
//    public String listarCinemas(Model model) {
//        model.addAttribute("cinemas", cinemaService.listarCinemas());
//        return "cinema/lista_Cinema";
//    }

    @GetMapping
    public String listarCinemas(Model model, @RequestParam(value = "termo", required = false) String termo) {

        List<Cinema> cinemas = cinemaService.listarCinemas(termo);

        System.out.println("Quantidade de cinemas encontrados: " + cinemas.size());
        if (!cinemas.isEmpty()) {
            System.out.println("Primeiro cinema da lista: " + cinemas.get(0).getNome());
        } else {
            System.out.println("A lista voltou VAZIA do banco de dados.");
        }
        System.out.println("--------------------------------------------------");

        model.addAttribute("cinemas", cinemas);
        model.addAttribute("termo", termo);
        return "cinema/lista_Cinema";
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

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        try {
            Cinema cinema = cinemaService.buscarCinemaPorId(id);
            model.addAttribute("cinema", cinema);
            model.addAttribute("tituloPagina", "Editar Cinema: " + cinema.getNome());
            return "cinema/form_Cinema";
        } catch (RuntimeException e) {
            return "redirect:/cinema";
        }
    }

    @PutMapping("/{id}")
    public String atualizarCinema(@PathVariable Long id, Cinema cinema, RedirectAttributes ra) {
        try {
            cinemaService.atualizarCinema(id, cinema);
            ra.addFlashAttribute("success", "Cinema atualizado com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erro ao atualizar cinema: " + e.getMessage());
        }
        return "redirect:/cinema";
    }

    @DeleteMapping("/{id}")
    public String deletarCinema(@PathVariable Long id, RedirectAttributes ra) {
        try {
            cinemaService.deletarCinema(id);
            ra.addFlashAttribute("success", "Cinema excluído com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erro ao excluir cinema: " + e.getMessage());
        }
        return "redirect:/cinema";
    }

}
