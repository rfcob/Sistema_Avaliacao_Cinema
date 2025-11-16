package br.uel.SistemaAvaliacaoCinema.repository;

import br.uel.SistemaAvaliacaoCinema.model.Criterio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class CriterioRepository {

    @Autowired
    private JdbcTemplate jdbc;

    private class CriterioRowMapper implements RowMapper<Criterio> {
        @Override
        public Criterio mapRow(ResultSet rs, int rowNum) throws SQLException {
            Criterio c = new Criterio();
            c.setIdCriterio(rs.getLong("id_criterio"));
            c.setNomeCriterio(rs.getString("nome_criterio"));
            c.setDescricao(rs.getString("descricao"));
            return c;
        }
    }

     public List<Criterio> listarTodos() {
        String sql = "SELECT id_criterio, nome_criterio, descricao FROM Criterio ORDER BY id_criterio";
        return jdbc.query(sql, new CriterioRowMapper());
    }
}