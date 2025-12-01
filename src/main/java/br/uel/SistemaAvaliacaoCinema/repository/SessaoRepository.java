
package br.uel.SistemaAvaliacaoCinema.repository;

import br.uel.SistemaAvaliacaoCinema.model.Sessao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.*;
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

            // Verifica se data_fim não é null
            Timestamp dataFim = rs.getTimestamp("data_fim");
            if (dataFim != null) {
                s.setDataFim(dataFim.toLocalDateTime());
            }

            s.setPreco(rs.getDouble("preco"));
            s.setIdioma(rs.getString("idioma"));
            s.setIdSala(rs.getLong("id_sala"));
            s.setIdFilme(rs.getLong("id_filme"));
            s.setAtivo(rs.getBoolean("ativo"));

            try {
                s.setNomeSala(rs.getString("tipo_sala"));
                s.setTituloFilme(rs.getString("titulo"));
            } catch (SQLException e) {
                // Ignora se não existir nas queries que não fazem JOIN
            }
            return s;
        }
    }

    public List<Sessao> findByFilters(Long cinemaId, String data, String hora) {
        StringBuilder sql = new StringBuilder(
                "SELECT se.*, sa.tipo_sala, f.titulo, c.nome AS nome_cinema " +
                        "FROM Sessao se " +
                        "JOIN Sala sa ON se.id_sala = sa.id_sala " +
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
            // Filtra pela hora do campo data_inicio
            sql.append("AND EXTRACT(HOUR FROM se.data_inicio) = ? ");
            params.add(Integer.parseInt(hora.split(":")[0]));
        }

        sql.append("ORDER BY se.data_inicio");

        return jdbcTemplate.query(sql.toString(), params.toArray(), new SessaoRowMapper());
    }

    public Optional<Sessao> findById(Long id) {
        String sql = "SELECT se.*, sa.tipo_sala, f.titulo " +
                "FROM Sessao se " +
                "LEFT JOIN Sala sa ON se.id_sala = sa.id_sala " +
                "LEFT JOIN Filme f ON se.id_filme = f.id_filme " +
                "WHERE se.id_sessao = ? AND se.ativo = true";
        try {
            Sessao s = jdbcTemplate.queryForObject(sql, new Object[]{id}, new SessaoRowMapper());
            return Optional.of(s);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void save(Sessao s) {
        if (s.getIdSessao() == null) {
            // INSERT - sem coluna 'hora'
            String sql = "INSERT INTO Sessao (data_inicio, data_fim, preco, idioma, id_sala, id_filme, ativo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setTimestamp(1, Timestamp.valueOf(s.getDataInicio()));

                // Data fim pode ser null
                if (s.getDataFim() != null) {
                    ps.setTimestamp(2, Timestamp.valueOf(s.getDataFim()));
                } else {
                    ps.setNull(2, Types.TIMESTAMP);
                }

                ps.setDouble(3, s.getPreco());
                ps.setString(4, s.getIdioma());
                ps.setLong(5, s.getIdSala());
                ps.setLong(6, s.getIdFilme());
                ps.setBoolean(7, s.isAtivo());
                return ps;
            }, keyHolder);

            // Obtém o ID gerado
            Number key = keyHolder.getKey();
            if (key != null) {
                s.setIdSessao(key.longValue());
            }
        } else {
            // UPDATE
            update(s);
        }
    }

    public int update(Sessao s) {
        String sql = "UPDATE Sessao SET data_inicio=?, data_fim=?, preco=?, idioma=?, id_sala=?, id_filme=?, ativo=? " +
                "WHERE id_sessao=?";
        return jdbcTemplate.update(sql,
                s.getDataInicio(),
                s.getDataFim(),
                s.getPreco(),
                s.getIdioma(),
                s.getIdSala(),
                s.getIdFilme(),
                s.isAtivo(),
                s.getIdSessao());
    }

    public int softDelete(Long id) {
        String sql = "UPDATE Sessao SET ativo=false WHERE id_sessao=?";
        return jdbcTemplate.update(sql, id);
    }
}