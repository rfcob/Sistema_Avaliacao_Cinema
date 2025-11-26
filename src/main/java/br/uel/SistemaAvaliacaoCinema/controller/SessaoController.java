package br.uel.SistemaAvaliacaoCinema.controller;

import br.uel.SistemaAvaliacaoCinema.model.Sessao;
import br.uel.SistemaAvaliacaoCinema.service.SessaoService;
import br.uel.SistemaAvaliacaoCinema.service.SalaService;
import br.uel.SistemaAvaliacaoCinema.service.FilmesService;
import br.uel.SistemaAvaliacaoCinema.service.CinemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/sessoes")
public class SessaoController {

    @Autowired
    private SessaoService sessaoService;
    @Autowired
    private SalaService salaService;
    @Autowired
    private FilmesService filmeService;
    @Autowired
    private CinemaService cinemaService;

    // Listagem com filtros
    @GetMapping
    public String listar(Model model,
                         @RequestParam(value = "cinemaId", required = false) Long cinemaId,
                         @RequestParam(value = "data", required = false) String data,
                         @RequestParam(value = "hora", required = false) String hora) {

        model.addAttribute("sessoes", sessaoService.listarSessoes(cinemaId, data, hora));

        // Para o filtro de cinemas, usamos o método padrão (sobrecarga) ou passamos null
        model.addAttribute("cinemas", cinemaService.listarCinemas());

        model.addAttribute("filtroCinemaId", cinemaId);
        model.addAttribute("filtroData", data);
        model.addAttribute("filtroHora", hora);
        return "sessoes/lista_sessoes";
    }

    // Formulário Novo
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("sessao", new Sessao());

        // CORREÇÃO AQUI: Passamos null, null para listar TODAS as salas sem filtrar
        model.addAttribute("salas", salaService.listarSalas(null, null));

        // CORREÇÃO AQUI: Passamos null para listar TODOS os filmes sem filtrar
        model.addAttribute("filmes", filmeService.listarFilmes(null));

        model.addAttribute("tituloPagina", "Cadastrar Nova Sessão");
        return "sessoes/form_sessoes";
    }

    @PostMapping
    public String salvar(@ModelAttribute Sessao sessao, RedirectAttributes ra) {
        try {
            sessaoService.cadastrarSessao(sessao);
            ra.addFlashAttribute("success", "Sessão cadastrada com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erro ao cadastrar sessão: " + e.getMessage());
        }
        return "redirect:/sessoes";
    }

    // Formulário de Edição
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            Sessao sessao = sessaoService.buscarPorId(id);
            model.addAttribute("sessao", sessao);

            // CORREÇÃO AQUI TAMBÉM: Passamos null nos parâmetros de busca
            model.addAttribute("salas", salaService.listarSalas(null, null));
            model.addAttribute("filmes", filmeService.listarFilmes(null));

            model.addAttribute("tituloPagina", "Editar Sessão: " + id);
            return "sessoes/form_sessoes";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/sessoes";
        }
    }

    @PutMapping("/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute Sessao sessao, RedirectAttributes ra) {
        try {
            sessaoService.atualizarSessao(id, sessao);
            ra.addFlashAttribute("success", "Sessão atualizada com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erro ao atualizar sessão: " + e.getMessage());
        }
        return "redirect:/sessoes";
    }

    @DeleteMapping("/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        try {
            sessaoService.excluirSessao(id);
            ra.addFlashAttribute("success", "Sessão excluída com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Erro ao excluir sessão: " + e.getMessage());
        }
        return "redirect:/sessoes";
    }
}