package br.uel.SistemaAvaliacaoCinema.service;

import br.uel.SistemaAvaliacaoCinema.model.Sala;
import br.uel.SistemaAvaliacaoCinema.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    // Lógica unificada de listagem e busca
    public List<Sala> listarSalas(Long cinemaId, String termo) {

        // 1. Se tiver termo de busca, usa a busca inteligente (ignora o filtro de ID por enquanto)
        if (termo != null && !termo.isBlank()) {
            return salaRepository.buscarPorTermo(termo);
        }

        // 2. Se não tiver termo, mas tiver ID do cinema, filtra por ID
        if (cinemaId != null && cinemaId > 0) {
            return salaRepository.findByCinemaId(cinemaId);
        }

        // 3. Se não tiver nada, traz tudo
        return salaRepository.findAll();
    }

    public Sala buscarSalaPorId(Long id) {
        return salaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala não encontrada com id: " + id));
    }

    public Sala cadastrarSala(Sala sala) {
        return salaRepository.save(sala);
    }

    public void atualizarSala(Long id, Sala sala) {
        sala.setIdSala(id);
        salaRepository.update(sala);
    }

    public void excluirSala(Long id) {
        int linhasAfetadas = salaRepository.softDeleteById(id);
        if (linhasAfetadas == 0) {
            throw new RuntimeException("Sala não encontrada ou já inativa com id: " + id);
        }
    }
}