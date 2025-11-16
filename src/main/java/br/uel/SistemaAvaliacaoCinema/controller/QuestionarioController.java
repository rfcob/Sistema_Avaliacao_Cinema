package br.uel.SistemaAvaliacaoCinema.controller;

import br.uel.SistemaAvaliacaoCinema.form.QuestionarioForm;
import br.uel.SistemaAvaliacaoCinema.form.RespostaCriterioForm;
import br.uel.SistemaAvaliacaoCinema.model.Criterio;
import br.uel.SistemaAvaliacaoCinema.repository.AvaliacaoRepository;
import br.uel.SistemaAvaliacaoCinema.repository.CriterioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class QuestionarioController {

    @Autowired
    private CriterioRepository criterioRepository;

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @GetMapping("/questionario")
    public String exibirPaginaQuestionario(@RequestParam("cliente") Long idCliente,
                                           @RequestParam("sessao") Long idSessao,
                                           Model model) {

        List<Criterio> criterios = criterioRepository.listarTodos();

        QuestionarioForm formulario = new QuestionarioForm();
        formulario.setIdCliente(idCliente);
        formulario.setIdSessao(idSessao);

        for (Criterio c : criterios) {
            formulario.getRespostas().add(new RespostaCriterioForm(c));
        }

        model.addAttribute("formulario", formulario);
        return "questionario/questionario";
    }

    @PostMapping("/questionario/salvar")
    public String salvarQuestionario(
            @ModelAttribute("formulario") QuestionarioForm formularioPreenchido
    ) {

        avaliacaoRepository.salvar(formularioPreenchido);

        return "redirect:/obrigado";
    }

    @GetMapping("/obrigado")
    public String paginaObrigado() {
        return "obrigado";
    }
}