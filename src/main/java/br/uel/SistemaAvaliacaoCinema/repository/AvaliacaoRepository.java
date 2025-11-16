package br.uel.SistemaAvaliacaoCinema.repository;

import br.uel.SistemaAvaliacaoCinema.model.Avaliacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AvaliacaoRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public List<Avaliacao> buscarAnonimas(Long cinemaId, Long sessaoId) {

        String sql = "SELECT a.*, s.id_sessao, sa.num_sala, f.titulo " +
                "FROM Avaliacao a " +
                "JOIN Sessao s ON a.id_sessao = s.id_sessao " +
                "JOIN Sala sa ON s.id_sala = sa.num_sala " +
                "JOIN Filme f ON s.id_filme = f.id_filme " +
                "WHERE a.comentario_anonimo = true ";

        if (cinemaId != null) sql += "AND sa.id_cinema = " + cinemaId + " ";
        if (sessaoId != null) sql += "AND s.id_sessao = " + sessaoId + " ";

        return jdbc.query(sql, (rs, rowNum) -> {
            Avaliacao a = new Avaliacao();
            a.setId(rs.getLong("id_avaliacao"));
            a.setNota(rs.getInt("nota_geral"));
            a.setComentario(rs.getString("comentario"));
            a.setAnonima(rs.getBoolean("comentario_anonimo"));
            a.setSessaoId(rs.getLong("id_sessao"));
            a.setSala(rs.getString("num_sala"));
            a.setFilme(rs.getString("titulo"));
            return a;
        });
    }

    public List<Avaliacao> buscarIdentificadas(Long cinemaId, Long sessaoId) {

        String sql = "SELECT a.*, s.id_sessao, sa.num_sala, f.titulo, c.nome AS cliente " +
                "FROM Avaliacao a " +
                "JOIN Sessao s ON a.id_sessao = s.id_sessao " +
                "JOIN Sala sa ON s.id_sala = sa.num_sala " +
                "JOIN Filme f ON s.id_filme = f.id_filme " +
                "JOIN Cliente c ON a.id_cliente = c.id_cliente " +
                "WHERE a.comentario_anonimo = false ";

        if (cinemaId != null) sql += "AND sa.id_cinema = " + cinemaId + " ";
        if (sessaoId != null) sql += "AND s.id_sessao = " + sessaoId + " ";

        return jdbc.query(sql, (rs, rowNum) -> {
            Avaliacao a = new Avaliacao();
            a.setId(rs.getLong("id_avaliacao"));
            a.setNota(rs.getInt("nota_geral"));
            a.setComentario(rs.getString("comentario"));
            a.setAnonima(rs.getBoolean("comentario_anonimo"));
            a.setSessaoId(rs.getLong("id_sessao"));
            a.setSala(rs.getString("num_sala"));
            a.setFilme(rs.getString("titulo"));
            a.setCliente(rs.getString("cliente"));
            return a;
        });
    }
}
