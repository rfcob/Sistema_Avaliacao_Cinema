package br.uel.SistemaAvaliacaoCinema.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class RelatorioRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> mediasPorCriterio(LocalDate inicio, LocalDate fim,
                                                       String faixaEtaria, Long idCinema,
                                                       String tipoSala, String sentimento,
                                                       Long idFilme) {
        StringBuilder sql = new StringBuilder("""
            SELECT c.nome_criterio,
                   AVG(ac.nota) AS media,
                   COUNT(a.id_avaliacao) AS total_avaliacoes
            FROM Avaliacao_Criterio ac
            JOIN Criterio c ON ac.id_criterio = c.id_criterio
            JOIN Avaliacao a ON ac.id_avaliacao = a.id_avaliacao
            JOIN Cliente cli ON a.id_cliente = cli.id_cliente
            JOIN Sessao s ON a.id_sessao = s.id_sessao
            JOIN Sala sa ON s.id_sala = sa.id_sala
            JOIN Cinema ci ON sa.id_cinema = ci.id_cinema
            JOIN Filme f ON s.id_filme = f.id_filme
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();
        aplicarFiltros(sql, params, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);

        sql.append(" GROUP BY c.nome_criterio ORDER BY media DESC");

        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
    }

    public List<Map<String, Object>> avaliacoesPorCinema(LocalDate inicio, LocalDate fim,
                                                         String faixaEtaria, Long idCinema,
                                                         String tipoSala, String sentimento,
                                                         Long idFilme) {
        StringBuilder sql = new StringBuilder("""
            SELECT ci.nome AS cinema_nome,
                   COUNT(a.id_avaliacao) AS total_avaliacoes
            FROM Avaliacao a
            JOIN Sessao s ON a.id_sessao = s.id_sessao
            JOIN Sala sa ON s.id_sala = sa.id_sala
            JOIN Cinema ci ON sa.id_cinema = ci.id_cinema
            JOIN Cliente cli ON a.id_cliente = cli.id_cliente
            JOIN Filme f ON s.id_filme = f.id_filme
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();
        aplicarFiltros(sql, params, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);

        sql.append(" GROUP BY ci.nome ORDER BY total_avaliacoes DESC");

        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
    }

    public List<Map<String, Object>> evolucaoTemporal(LocalDate inicio, LocalDate fim,
                                                      String faixaEtaria, Long idCinema,
                                                      String tipoSala, String sentimento,
                                                      Long idFilme) {
        StringBuilder sql = new StringBuilder("""
            SELECT DATE(a.data_avaliacao) AS data, 
                   AVG(a.nota_geral) AS media_dia,
                   COUNT(a.id_avaliacao) AS total_avaliacoes
            FROM Avaliacao a
            JOIN Cliente cli ON a.id_cliente = cli.id_cliente
            JOIN Sessao s ON a.id_sessao = s.id_sessao
            JOIN Sala sa ON s.id_sala = sa.id_sala
            JOIN Cinema ci ON sa.id_cinema = ci.id_cinema
            JOIN Filme f ON s.id_filme = f.id_filme
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();
        aplicarFiltros(sql, params, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);

        sql.append(" GROUP BY DATE(a.data_avaliacao) ORDER BY data ASC");

        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
    }

    public List<Map<String, Object>> notasPorSala(LocalDate inicio, LocalDate fim,
                                                  String faixaEtaria, Long idCinema,
                                                  String tipoSala, String sentimento,
                                                  Long idFilme) {
        StringBuilder sql = new StringBuilder("""
            SELECT sa.tipo_sala,
                   AVG(a.nota_geral) AS media,
                   COUNT(a.id_avaliacao) AS total_avaliacoes
            FROM Avaliacao a
            JOIN Sessao s ON a.id_sessao = s.id_sessao
            JOIN Sala sa ON s.id_sala = sa.id_sala
            JOIN Cinema ci ON sa.id_cinema = ci.id_cinema
            JOIN Cliente cli ON a.id_cliente = cli.id_cliente
            JOIN Filme f ON s.id_filme = f.id_filme
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();
        aplicarFiltros(sql, params, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);

        sql.append(" GROUP BY sa.tipo_sala ORDER BY media DESC");

        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
    }

    public List<Map<String, Object>> sentimentos(LocalDate inicio, LocalDate fim,
                                                 String faixaEtaria, Long idCinema,
                                                 String tipoSala, String sentimento,
                                                 Long idFilme) {
        StringBuilder sql = new StringBuilder("""
            SELECT a.sentimento_comentario,
                   COUNT(a.id_avaliacao) AS total
            FROM Avaliacao a
            JOIN Sessao s ON a.id_sessao = s.id_sessao
            JOIN Sala sa ON s.id_sala = sa.id_sala
            JOIN Cinema ci ON sa.id_cinema = ci.id_cinema
            JOIN Cliente cli ON a.id_cliente = cli.id_cliente
            JOIN Filme f ON s.id_filme = f.id_filme
            WHERE a.sentimento_comentario IS NOT NULL AND 1=1
        """);

        List<Object> params = new ArrayList<>();
        aplicarFiltros(sql, params, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);

        sql.append(" GROUP BY a.sentimento_comentario ORDER BY total DESC");

        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
    }

    public List<Map<String, Object>> avaliacoesPorFilme(LocalDate inicio, LocalDate fim,
                                                        String faixaEtaria, Long idCinema,
                                                        String tipoSala, String sentimento,
                                                        Long idFilme) {
        StringBuilder sql = new StringBuilder("""
            SELECT f.titulo,
                   AVG(a.nota_geral) AS media_geral,
                   COUNT(a.id_avaliacao) AS total_avaliacoes
            FROM Avaliacao a
            JOIN Sessao s ON a.id_sessao = s.id_sessao
            JOIN Sala sa ON s.id_sala = sa.id_sala
            JOIN Cinema ci ON sa.id_cinema = ci.id_cinema
            JOIN Cliente cli ON a.id_cliente = cli.id_cliente
            JOIN Filme f ON s.id_filme = f.id_filme
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();
        aplicarFiltros(sql, params, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);

        sql.append(" GROUP BY f.titulo ORDER BY total_avaliacoes DESC");

        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
    }

    public List<Map<String, Object>> avaliacoesPorFaixa(LocalDate inicio, LocalDate fim,
                                                        String faixaEtaria, Long idCinema,
                                                        String tipoSala, String sentimento,
                                                        Long idFilme) {
        StringBuilder sql = new StringBuilder("""
            SELECT
                CASE
                    WHEN EXTRACT(YEAR FROM AGE(CURRENT_DATE, cli.data_nascimento)) BETWEEN 18 AND 25 THEN '18-25'
                    WHEN EXTRACT(YEAR FROM AGE(CURRENT_DATE, cli.data_nascimento)) BETWEEN 26 AND 35 THEN '26-35'
                    WHEN EXTRACT(YEAR FROM AGE(CURRENT_DATE, cli.data_nascimento)) BETWEEN 36 AND 45 THEN '36-45'
                    WHEN EXTRACT(YEAR FROM AGE(CURRENT_DATE, cli.data_nascimento)) > 45 THEN '>45'
                    ELSE 'MENOR_IDADE_OU_OUTRO'
                END AS faixa_etaria,
                AVG(a.nota_geral) AS media_geral,
                COUNT(a.id_avaliacao) AS total_avaliacoes
            FROM Avaliacao a
            JOIN Cliente cli ON a.id_cliente = cli.id_cliente
            JOIN Sessao s ON a.id_sessao = s.id_sessao
            JOIN Sala sa ON s.id_sala = sa.id_sala
            JOIN Cinema ci ON sa.id_cinema = ci.id_cinema
            JOIN Filme f ON s.id_filme = f.id_filme
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();
        aplicarFiltros(sql, params, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);

        sql.append("""
             GROUP BY faixa_etaria
             ORDER BY faixa_etaria
        """);
        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
    }

    public List<Map<String, Object>> avaliacoesPorHora(LocalDate inicio, LocalDate fim,
                                                       String faixaEtaria, Long idCinema,
                                                       String tipoSala, String sentimento,
                                                       Long idFilme) {
        StringBuilder sql = new StringBuilder("""
            SELECT EXTRACT(HOUR FROM a.data_avaliacao) AS hora_dia,
                   AVG(a.nota_geral) AS media_hora,
                   COUNT(a.id_avaliacao) AS total_avaliacoes
            FROM Avaliacao a
            JOIN Cliente cli ON a.id_cliente = cli.id_cliente
            JOIN Sessao s ON a.id_sessao = s.id_sessao
            JOIN Sala sa ON s.id_sala = sa.id_sala
            JOIN Cinema ci ON sa.id_cinema = ci.id_cinema
            JOIN Filme f ON s.id_filme = f.id_filme
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();
        aplicarFiltros(sql, params, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);

        sql.append(" GROUP BY hora_dia ORDER BY hora_dia ASC");
        return jdbcTemplate.queryForList(sql.toString(), params.toArray());
    }

    private void aplicarFiltros(StringBuilder sql, List<Object> params,
                                LocalDate inicio, LocalDate fim, String faixaEtaria,
                                Long idCinema, String tipoSala, String sentimento,
                                Long idFilme) {

        if (inicio != null && fim != null) {
            sql.append(" AND a.data_avaliacao BETWEEN ? AND ? ");
            params.add(inicio);
            params.add(fim);
        } else {
            if (inicio != null) {
                sql.append(" AND a.data_avaliacao >= ? ");
                params.add(inicio);
            }
            if (fim != null) {
                sql.append(" AND a.data_avaliacao <= ? ");
                params.add(fim);
            }
        }

        if (idCinema != null) {
            sql.append(" AND ci.id_cinema = ? ");
            params.add(idCinema);
        }
        if (tipoSala != null && !tipoSala.isEmpty()) {
            sql.append(" AND sa.tipo_sala = ? ");
            params.add(tipoSala);
        }
        if (sentimento != null && !sentimento.isEmpty()) {
            sql.append(" AND a.sentimento_comentario = ? ");
            params.add(sentimento);
        }
        if (idFilme != null) {
            sql.append(" AND f.id_filme = ? ");
            params.add(idFilme);
        }
        if (faixaEtaria != null && !faixaEtaria.isEmpty()) {
            sql.append("""
                   AND CASE
                         WHEN EXTRACT(YEAR FROM AGE(CURRENT_DATE, cli.data_nascimento)) BETWEEN 18 AND 25 THEN '18-25'
                         WHEN EXTRACT(YEAR FROM AGE(CURRENT_DATE, cli.data_nascimento)) BETWEEN 26 AND 35 THEN '26-35'
                         WHEN EXTRACT(YEAR FROM AGE(CURRENT_DATE, cli.data_nascimento)) BETWEEN 36 AND 45 THEN '36-45'
                         WHEN EXTRACT(YEAR FROM AGE(CURRENT_DATE, cli.data_nascimento)) > 45 THEN '>45'
                         ELSE 'MENOR_IDADE_OU_OUTRO'
                       END = ?
            """);
            params.add(faixaEtaria);
        }
    }
}