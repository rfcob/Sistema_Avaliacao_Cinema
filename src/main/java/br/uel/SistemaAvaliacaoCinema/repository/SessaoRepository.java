package br.uel.SistemaAvaliacaoCinema.repository;

import br.uel.SistemaAvaliacaoCinema.model.Sessao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SessaoRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class SessaoRowMapper implements RowMapper<Sessao> {
        @Override
        public Sessao mapRow(ResultSet rs, int rowNum) throws SQLException {
            Sessao s = new Sessao();
            s.setIdSessao(rs.getLong("id_sessao"));
            s.setDataInicio(rs.getTimestamp("data_inicio").toLocalDateTime());
            s.setDataFim(rs.getTimestamp("data_fim").toLocalDateTime());
            s.setPreco(rs.getDouble("preco"));
            s.setHora(rs.getTime("hora").toLocalTime());
            s.setIdioma(rs.getString("idioma"));
            s.setIdSala(rs.getLong("id_sala"));
            s.setIdFilme(rs.getLong("id_filme"));
            s.setAtivo(rs.getBoolean("ativo"));

            try {
                s.setNomeSala(rs.getString("tipo_sala"));
                s.setTituloFilme(rs.getString("titulo"));
            } catch (SQLException e) {}
            return s;
        }
    }

    public List<Sessao> findByFilters(Long cinemaId, String data, String hora) {
        StringBuilder sql = new StringBuilder(
                "SELECT se.*, sa.tipo_sala, f.titulo, c.nome AS nome_cinema " +
                        "FROM Sessao se " +
                        "JOIN Sala sa ON se.id_sala = sa.num_sala " +
                        "JOIN Cinema c ON sa.id_cinema = c.id_cinema " +
                        "JOIN Filme f ON se.id_filme = f.id_filme " +
                        "WHERE se.ativo = true "
        );

        List<Object> params = new ArrayList<>();

        if (cinemaId != null) {
            sql.append("AND c.id_cinema = ? ");
            params.add(cinemaId);
        }
        if (data != null && !data.isEmpty()) {
            sql.append("AND DATE(se.data_inicio) = ? ");
            params.add(LocalDate.parse(data));
        }
        if (hora != null && !hora.isEmpty()) {
            sql.append("AND se.hora = ? ");
            params.add(LocalTime.parse(hora));
        }

        sql.append("ORDER BY se.data_inicio");

        return jdbcTemplate.query(sql.toString(), params.toArray(), new SessaoRowMapper());
    }

    public Optional<Sessao> findById(Long id) {
        String sql = "SELECT se.*, sa.tipo_sala, f.titulo " +
                "FROM Sessao se " +
                "LEFT JOIN Sala sa ON se.id_sala = sa.num_sala " +
                "LEFT JOIN Filme f ON se.id_filme = f.id_filme " +
                "WHERE se.id_sessao = ? AND se.ativo = true";
        try {
            Sessao s = jdbcTemplate.queryForObject(sql, new Object[]{id}, new SessaoRowMapper());
            return Optional.of(s);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int save(Sessao s) {
        String sql = "INSERT INTO Sessao (data_inicio, data_fim, preco, hora, idioma, id_sala, id_filme, ativo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, true)";
        return jdbcTemplate.update(sql,
                s.getDataInicio(),
                s.getDataFim(),
                s.getPreco(),
                s.getHora(),
                s.getIdioma(),
                s.getIdSala(),
                s.getIdFilme());
    }

    public int update(Sessao s) {
        String sql = "UPDATE Sessao SET data_inicio=?, data_fim=?, preco=?, hora=?, idioma=?, id_sala=?, id_filme=? " +
                "WHERE id_sessao=?";
        return jdbcTemplate.update(sql,
                s.getDataInicio(),
                s.getDataFim(),
                s.getPreco(),
                s.getHora(),
                s.getIdioma(),
                s.getIdSala(),
                s.getIdFilme(),
                s.getIdSessao());
    }

    public int softDelete(Long id) {
        String sql = "UPDATE Sessao SET ativo=false WHERE id_sessao=?";
        return jdbcTemplate.update(sql, id);
    }
}
