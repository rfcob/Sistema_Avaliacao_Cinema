package br.uel.SistemaAvaliacaoCinema.repository;

import br.uel.SistemaAvaliacaoCinema.model.Filme;
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
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class FilmesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private class FilmeRowMapper implements RowMapper<Filme> {
        @Override
        public Filme mapRow(ResultSet rs, int rowNum) throws SQLException {
            Filme filme = new Filme();
            filme.setIdFilme(rs.getLong("id_filme"));
            filme.setTitulo(rs.getString("titulo"));
            filme.setDuracao(rs.getInt("duracao"));
            filme.setGenero(rs.getString("genero"));
            filme.setElenco(rs.getString("elenco"));
            filme.setDirecao(rs.getString("direcao"));
            filme.setAnoProducao(rs.getInt("ano_producao"));
            return filme;
        }
    }

    public List<Filme> listarTodos() {
        String sql = "SELECT * FROM Filme ORDER BY titulo";
        return jdbcTemplate.query(sql, new FilmeRowMapper());
    }

    public Optional<Filme> procurarPorId(Long id) {
        String sql = "SELECT * FROM Filme WHERE id_filme = ?";
        try {
            Filme filme = jdbcTemplate.queryForObject(sql, new Object[]{id}, new FilmeRowMapper());
            return Optional.of(filme);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Filme incluirNovo(Filme filme) {
        String sql = "INSERT INTO Filme (titulo, duracao, genero, elenco, direcao, ano_producao) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id_filme"});
            ps.setString(1, filme.getTitulo());
            ps.setObject(2, filme.getDuracao());
            ps.setString(3, filme.getGenero());
            ps.setString(4, filme.getElenco());
            ps.setString(5, filme.getDirecao());
            ps.setObject(6, filme.getAnoProducao());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            filme.setIdFilme(keyHolder.getKey().longValue());
        }
        return filme;
    }

    public int update(Filme filme) {
        String sql = "UPDATE Filme SET titulo = ?, duracao = ?, genero = ?, " +
                "elenco = ?, direcao = ?, ano_producao = ? " +
                "WHERE id_filme = ?";

        return jdbcTemplate.update(sql,
                filme.getTitulo(),
                filme.getDuracao(),
                filme.getGenero(),
                filme.getElenco(),
                filme.getDirecao(),
                filme.getAnoProducao(),
                filme.getIdFilme()); // ID é o último
    }

    public int deletePorId(Long id) {
        String sql = "DELETE FROM Filme WHERE id_filme = ?";
        return jdbcTemplate.update(sql, id);
    }
}