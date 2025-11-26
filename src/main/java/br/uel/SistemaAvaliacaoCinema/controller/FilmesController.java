package br.uel.SistemaAvaliacaoCinema.controller;

import br.uel.SistemaAvaliacaoCinema.model.Filme;
import br.uel.SistemaAvaliacaoCinema.service.FilmesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/filmes")
public class FilmesController {

    @Autowired
    private FilmesService filmesService;

        // Recebe o termo de busca
        @GetMapping
        public String listarFilmes(Model model, @RequestParam(value = "termo", required = false) String termo) {
            model.addAttribute("filmes", filmesService.listarFilmes(termo));
            model.addAttribute("termo", termo); // Devolve para o HTML manter o campo preenchido
            return "filmes/lista_filmes";
        }

        @GetMapping("/novo")
        public String mostrarFormularioNovo(Model model) {
            model.addAttribute("filme", new Filme());
            model.addAttribute("tituloPagina", "Cadastrar Novo Filme");
            return "filmes/form_filmes";
        }

        @PostMapping
        public String salvarFilme(Filme filme, RedirectAttributes ra) {
            try {
                filmesService.cadastrarFilme(filme);
                ra.addFlashAttribute("success", "Filme cadastrado com sucesso!");
            } catch (Exception e) {
                ra.addFlashAttribute("error", "Erro ao cadastrar filme: " + e.getMessage());
            }
            return "redirect:/filmes";
        }

        @GetMapping("/editar/{id}")
        public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes ra) {
            try {
                Filme filme = filmesService.buscarFilmePorId(id);
                model.addAttribute("filme", filme);
                model.addAttribute("tituloPagina", "Editar Filme: " + filme.getTitulo());
                return "filmes/form_filmes";
            } catch (RuntimeException e) {
                ra.addFlashAttribute("error", e.getMessage());
                return "redirect:/filmes";
            }
        }

        @PutMapping("/{id}")
        public String atualizarFilme(@PathVariable Long id, Filme filme, RedirectAttributes ra) {
            try {
                filmesService.atualizarFilme(id, filme);
                ra.addFlashAttribute("success", "Filme atualizado com sucesso!");
            } catch (Exception e) {
                ra.addFlashAttribute("error", "Erro ao atualizar filme: " + e.getMessage());
            }
            return "redirect:/filmes";
        }

        @DeleteMapping("/{id}") // Mudei para apenas /{id} para padronizar com os outros
        public String deletarFilme(@PathVariable Long id, RedirectAttributes ra) {
            try {
                filmesService.excluirFilme(id);
                ra.addFlashAttribute("success", "Filme excluído com sucesso!");
            } catch (Exception e) {
                ra.addFlashAttribute("error", "Erro ao excluir filme: " + e.getMessage());
            }
            return "redirect:/filmes";
        }
}