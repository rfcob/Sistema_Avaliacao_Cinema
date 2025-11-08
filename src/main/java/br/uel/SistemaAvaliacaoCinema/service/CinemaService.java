package br.uel.SistemaAvaliacaoCinema.service;

import br.uel.SistemaAvaliacaoCinema.model.Cinema;
import br.uel.SistemaAvaliacaoCinema.repository.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaService {

    @Autowired
    private CinemaRepository cinemaRepository;

    public List<Cinema>  listarCinemas() {
        return cinemaRepository.findAll();
    }

    public Cinema buscarCinemaPorId(Long id) {
        return cinemaRepository.findById(id).orElseThrow( () -> new RuntimeException("Cinema não cadastrado"));
    }

    public Cinema buscarCinemaNome(String nome) {
        return cinemaRepository.findByNome(nome).orElseThrow( () -> new RuntimeException("Cinema não cadastrado"));
    }



}
