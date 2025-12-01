//package br.uel.SistemaAvaliacaoCinema.service;
//
//import br.uel.SistemaAvaliacaoCinema.model.Cinema;
//import br.uel.SistemaAvaliacaoCinema.repository.CinemaRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class CinemaService {
//
//    @Autowired
//    private CinemaRepository cinemaRepository;
//
//    public List<Cinema>  listarCinemas() {
//        return cinemaRepository.findAll();
//    }
//
//    public Cinema buscarCinemaPorId(Long id) {
//        return cinemaRepository.findById(id).orElseThrow( () -> new RuntimeException("Cinema não cadastrado"));
//    }
//
//    public Cinema buscarCinemaNome(String nome) {
//        return cinemaRepository.findByNome(nome).orElseThrow( () -> new RuntimeException("Cinema não cadastrado"));
//    }
//
//    public void cadastrarCinema(Cinema cinema) {
//        cinemaRepository.save(cinema);
//    }
//
//    public void atualizarCinema(Long id, Cinema cinema) {
//        cinema.setIdCinema(id);
//        cinemaRepository.update(cinema);
//    }
//
//    public void deletarCinema(Long id) {
//        cinemaRepository.deleteById(id);
//    }
//
//}

package br.uel.SistemaAvaliacaoCinema.service;

import br.uel.SistemaAvaliacaoCinema.model.Cinema;
import br.uel.SistemaAvaliacaoCinema.repository.CinemaRepository;
import br.uel.SistemaAvaliacaoCinema.repository.SalaRepository; // Importe o SalaRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CinemaService {

    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private SalaRepository salaRepository; // Injetamos o repositório de salas

    /* METODO PRINCIPAL COM A LÓGICA DE PREENCHIMENTO */
    public List<Cinema> listarCinemas(String termo) {
        List<Cinema> cinemas;

        // 1. Busca os cinemas (Filtrados ou Todos)
        if (termo != null && !termo.isBlank()) {
            cinemas = cinemaRepository.buscarPorTermo(termo);
        } else {
            cinemas = cinemaRepository.findAll();
        }

        // 2. Para cada cinema, buscamos os tipos de sala e montamos a string
        for (Cinema c : cinemas) {
            List<String> tipos = salaRepository.buscarTiposDeSalaPorCinema(c.getIdCinema());

            if (tipos.isEmpty()) {
                c.setResumoTiposSalas("-");
            } else {
                // Junta a lista em uma string separada por vírgula (Ex: "VIP, IMAX")
                c.setResumoTiposSalas(String.join(", ", tipos));
            }
        }

        return cinemas;
    }

    // Sobrecarga de compatibilidade
    public List<Cinema> listarCinemas() {
        return listarCinemas(null);
    }

    public Cinema buscarCinemaPorId(Long id) {
        return cinemaRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("Cinema não cadastrado"));
    }

    public Cinema buscarCinemaNome(String nome) {
        return cinemaRepository.findByNome(nome)
                .orElseThrow( () -> new RuntimeException("Cinema não cadastrado"));
    }

    public void cadastrarCinema(Cinema cinema) {
        cinemaRepository.save(cinema);
    }

    public void atualizarCinema(Long id, Cinema cinema) {
        cinema.setIdCinema(id);
        cinemaRepository.update(cinema);
    }

    public void deletarCinema(Long id) {
        cinemaRepository.deleteById(id);
    }


}

