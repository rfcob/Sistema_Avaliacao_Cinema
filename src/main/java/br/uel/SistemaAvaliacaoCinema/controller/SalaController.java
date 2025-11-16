package br.uel.SistemaAvaliacaoCinema.controller;

import br.uel.SistemaAvaliacaoCinema.model.Cinema;
import br.uel.SistemaAvaliacaoCinema.model.Sala;
import br.uel.SistemaAvaliacaoCinema.service.CinemaService;
import br.uel.SistemaAvaliacaoCinema.service.SalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cineSalas")
public class SalaController {

    @Autowired
    private SalaService salaService;


    @Autowired
    private CinemaService cinemaService;


    @GetMapping
    public String listarSalas(@RequestParam(value = "cinemaId", required = false) Long cinemaId, Model model) {
        List<Sala> salas = salaService.listarSalas(cinemaId);
        List<Cinema> cinemas = cinemaService.listarCinemas();

        model.addAttribute("salas", salas);
        model.addAttribute("cinemas", cinemas);
        model.addAttribute("filtroCinemaId", cinemaId);

        return "sala/lista_salas";
    }

    @GetMapping("/novo")
    public String mostrarFormularioNovo(Model model) {
        model.addAttribute("sala", new Sala());
        model.addAttribute("cinemas", cinemaService.listarCinemas());
        model.addAttribute("tituloPagina", "Cadastrar Nova Sala");
        return "sala/form_salas";
    }

    @PostMapping
    public String salvarSala(Sala sala, RedirectAttributes ra) {
        try {
            salaService.cadastrarSala(sala);
            ra.addFlashAttribute("success", "Sala cadastrada com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erro ao cadastrar sala: " + e.getMessage());
        }
        return "redirect:/cineSalas";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            Sala sala = salaService.buscarSalaPorId(id);
            model.addAttribute("sala", sala);
            model.addAttribute("cinemas", cinemaService.listarCinemas());
            model.addAttribute("tituloPagina", "Editar Sala: " + id);
            return "sala/form_salas"; // Reutiliza o mesmo formulário
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/cineSalas";
        }
    }

    @PutMapping("/{id}")
    public String atualizarSala(@PathVariable Long id, Sala sala, RedirectAttributes ra) {
        try {
            salaService.atualizarSala(id, sala);
            ra.addFlashAttribute("success", "Sala atualizada com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erro ao atualizar sala: " + e.getMessage());
        }
        return "redirect:/cineSalas";
    }

    @DeleteMapping("/{id}")
    public String deletarSala(@PathVariable Long id, RedirectAttributes ra) {
        try {
            salaService.excluirSala(id);
            ra.addFlashAttribute("success", "Sala excluída com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erro ao excluir sala: " + e.getMessage());
        }
        return "redirect:/cineSalas";
    }
}