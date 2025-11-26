package br.uel.SistemaAvaliacaoCinema.repository;

import br.uel.SistemaAvaliacaoCinema.model.Cinema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CinemaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper simples e direto (sem tentar buscar colunas de salas)
    private class CinemaRowMapper implements RowMapper<Cinema> {
        @Override
        public Cinema mapRow(ResultSet rs, int rowNum) throws SQLException {
            Cinema cinema = new Cinema();
            cinema.setIdCinema(rs.getLong("id_cinema"));
            cinema.setNome(rs.getString("nome"));
            cinema.setLocalizacao(rs.getString("localizacao"));
            cinema.setTipoEstabelecimento(rs.getString("tipo_estabelecimento"));
            cinema.setNumeroSalas(rs.getInt("numero_salas"));

            // Define um valor padrão para não quebrar o HTML que espera esse campo
            cinema.setResumoTiposSalas("Ver detalhes na edição");

            return cinema;
        }
    }

    // 1. Busca Todos (Simples, sem JOIN)
    public List<Cinema> findAll() {
        String sql = "SELECT * FROM Cinema ORDER BY nome";
        return jdbcTemplate.query(sql, new CinemaRowMapper());
    }

    // 2. Busca Filtrada (Simples, sem JOIN)
    public List<Cinema> buscarPorTermo(String termo) {
        // Imprime no console para você ter certeza que a busca chegou aqui
        System.out.println("Buscando cinemas por: " + termo);

        String sql = "SELECT * FROM Cinema WHERE " +
                "nome ILIKE ? OR " +
                "localizacao ILIKE ? OR " +
                "tipo_estabelecimento ILIKE ? " +
                "ORDER BY nome";

        String pattern = "%" + termo + "%";
        return jdbcTemplate.query(sql, new Object[]{pattern, pattern, pattern}, new CinemaRowMapper());
    }

    // --- Métodos Padrão (FindById, Save, Update, Delete) ---

    public Optional<Cinema> findById(Long id) {
        String sql = "SELECT * FROM Cinema WHERE id_cinema = ?";
        try {
            Cinema cinema = jdbcTemplate.queryForObject(sql, new Object[]{id}, new CinemaRowMapper());
            return Optional.of(cinema);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Cinema> findByNome(String nome) {
        String sql = "SELECT * FROM Cinema WHERE nome = ?";
        try {
            Cinema cinema = jdbcTemplate.queryForObject(sql, new Object[]{nome}, new CinemaRowMapper());
            return Optional.of(cinema);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Cinema save(Cinema cinema) {
        String sql = "INSERT INTO Cinema (nome, localizacao, tipo_estabelecimento, numero_salas) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id_cinema"});
            ps.setString(1, cinema.getNome());
            ps.setString(2, cinema.getLocalizacao());
            ps.setString(3, cinema.getTipoEstabelecimento());
            ps.setInt(4, cinema.getNumeroSalas());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            cinema.setIdCinema(keyHolder.getKey().longValue());
        }
        return cinema;
    }

    public int update(Cinema cinema) {
        String sql = "UPDATE Cinema SET nome = ?, localizacao = ?, tipo_estabelecimento = ?, numero_salas = ? WHERE id_cinema = ?";
        return jdbcTemplate.update(sql, cinema.getNome(), cinema.getLocalizacao(), cinema.getTipoEstabelecimento(), cinema.getNumeroSalas(), cinema.getIdCinema());
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM Cinema WHERE id_cinema = ?";
        return jdbcTemplate.update(sql, id);
    }
}