package br.uel.SistemaAvaliacaoCinema.repository;

import br.uel.SistemaAvaliacaoCinema.model.Sala;
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
public class SalaRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class SalaRowMapper implements RowMapper<Sala> {
        @Override
        public Sala mapRow(ResultSet rs, int rowNum) throws SQLException {
            Sala sala = new Sala();
            sala.setNumSala(rs.getLong("num_sala"));
            sala.setCapacidade(rs.getInt("capacidade"));
            sala.setTipoSom(rs.getString("tipo_som"));
            sala.setFormatoExibicao(rs.getString("formato_exibicao"));
            sala.setTecnologia(rs.getString("tecnologia"));
            sala.setTipoSala(rs.getString("tipo_sala"));
            sala.setIdCinema(rs.getLong("id_cinema"));
            sala.setAtivo(rs.getBoolean("ativo")); // Adicionado

            try {
                sala.setNomeCinema(rs.getString("nome_cinema"));
            } catch (SQLException e) {
                // Ignora se a coluna "nome_cinema" não existir
            }
            return sala;
        }
    }


    public List<Sala> findAll() {
        String sql = "SELECT s.*, c.nome as nome_cinema FROM Sala s " +
                "LEFT JOIN Cinema c ON s.id_cinema = c.id_cinema " +
                "WHERE s.ativo = true " + // FILTRO ADICIONADO
                "ORDER BY c.nome, s.num_sala";
        return jdbcTemplate.query(sql, new SalaRowMapper());
    }


    public Optional<Sala> findById(Long id) {
        String sql = "SELECT s.*, c.nome as nome_cinema FROM Sala s " +
                "LEFT JOIN Cinema c ON s.id_cinema = c.id_cinema " +
                "WHERE s.num_sala = ? AND s.ativo = true"; // FILTRO ADICIONADO
        try {
            Sala sala = jdbcTemplate.queryForObject(sql, new Object[]{id}, new SalaRowMapper());
            return Optional.of(sala);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    public List<Sala> findByCinemaId(Long cinemaId) {
        String sql = "SELECT s.*, c.nome as nome_cinema FROM Sala s " +
                "LEFT JOIN Cinema c ON s.id_cinema = c.id_cinema " +
                "WHERE s.id_cinema = ? AND s.ativo = true " + // FILTRO ADICIONADO
                "ORDER BY s.num_sala";
        return jdbcTemplate.query(sql, new Object[]{cinemaId}, new SalaRowMapper());
    }


    public Sala save(Sala sala) {
        // SQL atualizado para incluir 'ativo'
        String sql = "INSERT INTO Sala (capacidade, tipo_som, formato_exibicao, tecnologia, tipo_sala, id_cinema, ativo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"num_sala"});
            ps.setObject(1, sala.getCapacidade());
            ps.setString(2, sala.getTipoSom());
            ps.setString(3, sala.getFormatoExibicao());
            ps.setString(4, sala.getTecnologia());
            ps.setString(5, sala.getTipoSala());
            ps.setLong(6, sala.getIdCinema());
            ps.setBoolean(7, true); // Garante que é salvo como ativo
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            sala.setNumSala(keyHolder.getKey().longValue());
        }
        return sala;
    }


    public int update(Sala sala) {
        // (Nota: Este update não altera o status 'ativo')
        String sql = "UPDATE Sala SET capacidade = ?, tipo_som = ?, formato_exibicao = ?, " +
                "tecnologia = ?, tipo_sala = ?, id_cinema = ? " +
                "WHERE num_sala = ?";

        return jdbcTemplate.update(sql,
                sala.getCapacidade(),
                sala.getTipoSom(),
                sala.getFormatoExibicao(),
                sala.getTecnologia(),
                sala.getTipoSala(),
                sala.getIdCinema(),
                sala.getNumSala());
    }


    public int softDeleteById(Long id) {
        String sql = "UPDATE Sala SET ativo = false WHERE num_sala = ?";
        return jdbcTemplate.update(sql, id);
    }
}