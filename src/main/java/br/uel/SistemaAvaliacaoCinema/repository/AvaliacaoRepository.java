package br.uel.SistemaAvaliacaoCinema.repository;

import br.uel.SistemaAvaliacaoCinema.form.QuestionarioForm;
import br.uel.SistemaAvaliacaoCinema.form.RespostaCriterioForm;
import br.uel.SistemaAvaliacaoCinema.model.Avaliacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AvaliacaoRepository {

    @Autowired
    private JdbcTemplate jdbc;

    @Transactional
    public void salvar(QuestionarioForm formulario) {

        List<RespostaCriterioForm> respostas = formulario.getRespostas();

        double soma = 0;
        for (RespostaCriterioForm resposta : respostas) {
            if (resposta.getNota() != null) {
                soma += resposta.getNota().doubleValue();
            }
        }

        BigDecimal notaGeral = BigDecimal.ZERO;
        if (!respostas.isEmpty()) {
            notaGeral = BigDecimal.valueOf(soma / respostas.size())
                    .setScale(1, RoundingMode.HALF_UP);
        }

        final BigDecimal notaGeralFinal = notaGeral;

        String sqlAvaliacao = "INSERT INTO Avaliacao (comentario, id_cliente, id_sessao, comentario_anonimo, nota_geral) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlAvaliacao, new String[]{"id_avaliacao"});
            ps.setString(1, formulario.getComentario());
            ps.setLong(2, formulario.getIdCliente());
            ps.setLong(3, formulario.getIdSessao());
            ps.setBoolean(4, formulario.isComentarioAnonimo());

            ps.setBigDecimal(5, notaGeralFinal);


            return ps;
        }, keyHolder);

        long idAvaliacaoSalva = keyHolder.getKey().longValue();


        String sqlRespostas = "INSERT INTO Avaliacao_Criterio (nota, id_avaliacao, id_criterio) VALUES (?, ?, ?)";

        jdbc.batchUpdate(sqlRespostas, respostas, respostas.size(),
                (PreparedStatement ps, RespostaCriterioForm resposta) -> {
                    ps.setBigDecimal(1, resposta.getNota());
                    ps.setLong(2, idAvaliacaoSalva);
                    ps.setLong(3, resposta.getCriterio().getIdCriterio());
                });
    }


    public List<Avaliacao> buscarAnonimas(Long cinemaId, Long sessaoId) {

        String sql = "SELECT a.id_avaliacao, a.nota_geral, a.comentario, a.comentario_anonimo, " +
                "s.id_sessao, s.id_sala, f.titulo " +
                "FROM Avaliacao a " +
                "JOIN Sessao s ON a.id_sessao = s.id_sessao " +
                "JOIN Sala sa ON s.id_sala = sa.id_sala " +
                "JOIN Filme f ON s.id_filme = f.id_filme " +
                "WHERE a.comentario_anonimo = true ";

        List<Object> params = new ArrayList<>();

        if (cinemaId != null) {
            sql += "AND sa.id_cinema = ? ";
            params.add(cinemaId);
        }

        if (sessaoId != null) {
            sql += "AND s.id_sessao = ? ";
            params.add(sessaoId);
        }

        return jdbc.query(sql, params.toArray(), (rs, rowNum) -> {
            Avaliacao a = new Avaliacao();
            a.setId(rs.getLong("id_avaliacao"));
            a.setNota(rs.getDouble("nota_geral"));
            a.setComentario(rs.getString("comentario"));
            a.setAnonima(rs.getBoolean("comentario_anonimo"));
            a.setSessaoId(rs.getLong("id_sessao"));
            a.setIdSala(rs.getLong("id_sala"));
            a.setFilme(rs.getString("titulo"));
            return a;
        });
    }

    public List<Avaliacao> buscarIdentificadas(Long cinemaId, Long sessaoId) {

        String sql = "SELECT a.id_avaliacao, a.nota_geral, a.comentario, a.comentario_anonimo, " +
                "s.id_sessao, s.id_sala, f.titulo, c.nome AS cliente " +
                "FROM Avaliacao a " +
                "JOIN Sessao s ON a.id_sessao = s.id_sessao " +
                "JOIN Sala sa ON s.id_sala = sa.id_sala " +
                "JOIN Filme f ON s.id_filme = f.id_filme " +
                "JOIN Cliente c ON a.id_cliente = c.id_cliente " +
                "WHERE a.comentario_anonimo = false ";

        List<Object> params = new ArrayList<>();

        if (cinemaId != null) {
            sql += "AND sa.id_cinema = ? ";
            params.add(cinemaId);
        }

        if (sessaoId != null) {
            sql += "AND s.id_sessao = ? ";
            params.add(sessaoId);
        }

        return jdbc.query(sql, params.toArray(), (rs, rowNum) -> {
            Avaliacao a = new Avaliacao();
            a.setId(rs.getLong("id_avaliacao"));
            a.setNota(rs.getDouble("nota_geral"));
            a.setComentario(rs.getString("comentario"));
            a.setAnonima(rs.getBoolean("comentario_anonimo"));
            a.setSessaoId(rs.getLong("id_sessao"));
            a.setIdSala(rs.getLong("id_sala"));
            a.setFilme(rs.getString("titulo"));
            a.setCliente(rs.getString("cliente"));
            return a;
        });
    }
}