package br.uel.SistemaAvaliacaoCinema.service;

import br.uel.SistemaAvaliacaoCinema.model.Cliente;
import br.uel.SistemaAvaliacaoCinema.model.Filme;
import br.uel.SistemaAvaliacaoCinema.repository.FilmesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmesService {

    @Autowired
    private FilmesRepository filmeRepository;

    public List<Filme> listarFilmes() {
        return filmeRepository.listarTodos();
    }

    public Filme buscarFilmePorId(Long id) {
        return filmeRepository.procurarPorId(id)
                .orElseThrow(() -> new RuntimeException("Filme não encontrado com id: " + id));
    }

    public Filme cadastrarFilme(Filme filme) {
         return filmeRepository.incluirNovo(filme);
    }

    public void atualizarFilme(Long id, Filme filme) {
        filme.setIdFilme(id);
        filmeRepository.update(filme);
    }

    public void excluirFilme(Long id) {
        filmeRepository.deletePorId(id);
    }

    // PAra busca
    public List<Filme> listarFilmes(String termo) {
        if (termo != null && !termo.isBlank()) {
            return filmeRepository.buscarPorTermo(termo);
        }
        // Se não tiver termo, lista todos
        return filmeRepository.listarTodos();
    }
}