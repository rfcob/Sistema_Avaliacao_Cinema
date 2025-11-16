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


    public List<Sala> listarSalas(Long cinemaId) {
        if (cinemaId != null && cinemaId > 0) {
            return salaRepository.findByCinemaId(cinemaId);
        } else {
            return salaRepository.findAll();
        }
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