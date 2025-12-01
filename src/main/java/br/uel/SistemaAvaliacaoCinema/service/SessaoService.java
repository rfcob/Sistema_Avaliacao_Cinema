package br.uel.SistemaAvaliacaoCinema.service;

import br.uel.SistemaAvaliacaoCinema.model.Sessao;
import br.uel.SistemaAvaliacaoCinema.repository.SessaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessaoService {

    @Autowired
    private SessaoRepository repo;

    public List<Sessao> listarSessoes(Long cinemaId, String data, String hora) {
        return repo.findByFilters(cinemaId, data, hora);
    }

    public Sessao buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada com id: " + id));
    }

    public void cadastrarSessao(Sessao s) {
        repo.save(s);
    }

    public void atualizarSessao(Long id, Sessao s) {
        s.setIdSessao(id);
        repo.update(s);
    }

    public void excluirSessao(Long id) {
        int linhas = repo.softDelete(id);
        if (linhas == 0) throw new RuntimeException("Sessão não encontrada ou já inativa.");
    }
}