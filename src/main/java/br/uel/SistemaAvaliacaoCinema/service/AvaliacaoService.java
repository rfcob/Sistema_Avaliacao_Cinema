package br.uel.SistemaAvaliacaoCinema.service;

import br.uel.SistemaAvaliacaoCinema.model.Avaliacao;
import br.uel.SistemaAvaliacaoCinema.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository repo;

    public List<Avaliacao> listarAvaliacoesAnonimas(Long cinemaId, Long sessaoId) {
        return repo.buscarAnonimas(cinemaId, sessaoId);
    }

    public List<Avaliacao> listarAvaliacoesIdentificadas(Long cinemaId, Long sessaoId) {
        return repo.buscarIdentificadas(cinemaId, sessaoId);
    }
}
