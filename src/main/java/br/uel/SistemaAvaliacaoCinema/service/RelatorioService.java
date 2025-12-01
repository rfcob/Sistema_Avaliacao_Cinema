package br.uel.SistemaAvaliacaoCinema.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RelatorioService {

    private final DataSource dataSource;

    @Value("${app.relatorios.diretorio:./relatorios/}")
    private String diretorioRelatorios;

    public RelatorioService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void limparGraficos() {
        File dir = new File(diretorioRelatorios);
        if (dir.exists() && dir.isDirectory()) {
            File[] arquivos = dir.listFiles((d, nome) -> nome.toLowerCase().endsWith(".png"));
            if (arquivos != null) {
                for (File arquivo : arquivos) {
                    if (!arquivo.delete()) {
                        System.err.println("Aviso: Não foi possível deletar " + arquivo.getName());
                    }
                }
            }
        }
    }

    public boolean verificarGraficosGerados() {
        File dir = new File(diretorioRelatorios);
        if (!dir.exists() || !dir.isDirectory()) {
            return false;
        }
        File[] arquivos = dir.listFiles((d, nome) -> nome.toLowerCase().endsWith(".png"));
        return (arquivos != null && arquivos.length > 0);
    }

    public String exportarDadosParaJson() throws Exception {
        return gerarJson(null, null, null, null, null, null, null);
    }

    public String gerarJsonComFiltros(LocalDate inicio,
                                      LocalDate fim,
                                      String faixaEtaria,
                                      Long idCinema,
                                      String tipoSala,
                                      String sentimento,
                                      Long idFilme) throws Exception {
        return gerarJson(inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);
    }

    private String gerarJson(LocalDate inicio,
                             LocalDate fim,
                             String faixaEtaria,
                             Long idCinema,
                             String tipoSala,
                             String sentimento,
                             Long idFilme) throws Exception {

        String arquivoJson = diretorioRelatorios + "dados_relatorio.json";
        File dir = new File(diretorioRelatorios);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new Exception("Não foi possível criar o diretório: " + diretorioRelatorios);
            }
        }

        try (Connection conn = dataSource.getConnection();
             FileWriter writer = new FileWriter(arquivoJson)) {

            StringBuilder jsonContent = new StringBuilder();
            jsonContent.append("{\n");

            // Array para armazenar todas as consultas
            String[] consultas = new String[8];

            try {
                consultas[0] = consultarMediasPorCriterio(conn, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);
                consultas[1] = consultarAvaliacoesPorCinema(conn, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);
                consultas[2] = consultarEvolucaoTemporal(conn, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);
                consultas[3] = consultarNotasPorSala(conn, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);
                consultas[4] = consultarSentimentos(conn, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);
                consultas[5] = consultarAvaliacoesPorFilme(conn, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);
                consultas[6] = consultarAvaliacoesPorFaixa(conn, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);
                consultas[7] = consultarAvaliacoesPorHora(conn, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);
            } catch (Exception e) {
                throw new Exception("Erro ao executar consultas para relatório: " + e.getMessage(), e);
            }

            // Construir JSON
            jsonContent.append("\"medias_por_criterio\": ").append(consultas[0]).append(",\n");
            jsonContent.append("\"avaliacoes_por_cinema\": ").append(consultas[1]).append(",\n");
            jsonContent.append("\"evolucao_temporal\": ").append(consultas[2]).append(",\n");
            jsonContent.append("\"notas_por_sala\": ").append(consultas[3]).append(",\n");
            jsonContent.append("\"sentimentos\": ").append(consultas[4]).append(",\n");
            jsonContent.append("\"avaliacoes_por_filme\": ").append(consultas[5]).append(",\n");
            jsonContent.append("\"avaliacoes_por_faixa\": ").append(consultas[6]).append(",\n");
            jsonContent.append("\"avaliacoes_por_hora\": ").append(consultas[7]).append("\n");

            jsonContent.append("}");

            writer.write(jsonContent.toString());
            return arquivoJson;
        }
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
            sql.append(" AND CASE ")
                    .append("   WHEN EXTRACT(YEAR FROM AGE(CURRENT_DATE, cl.data_nascimento)) BETWEEN 18 AND 25 THEN '18-25' ")
                    .append("   WHEN EXTRACT(YEAR FROM AGE(CURRENT_DATE, cl.data_nascimento)) BETWEEN 26 AND 35 THEN '26-35' ")
                    .append("   WHEN EXTRACT(YEAR FROM AGE(CURRENT_DATE, cl.data_nascimento)) BETWEEN 36 AND 45 THEN '36-45' ")
                    .append("   WHEN EXTRACT(YEAR FROM AGE(CURRENT_DATE, cl.data_nascimento)) > 45 THEN '>45' ")
                    .append("   ELSE 'MENOR_IDADE_OU_OUTRO' ")
                    .append(" END = ? ");
            params.add(faixaEtaria);
        }
    }

    private String consultarMediasPorCriterio(Connection conn, LocalDate inicio, LocalDate fim,
                                              String faixaEtaria, Long idCinema, String tipoSala,
                                              String sentimento, Long idFilme) throws Exception {
        StringBuilder sql = new StringBuilder("""
            SELECT c.nome_criterio,
                   AVG(ac.nota) AS media
            FROM Avaliacao_Criterio ac
            JOIN Criterio c ON ac.id_criterio = c.id_criterio
            JOIN Avaliacao a ON ac.id_avaliacao = a.id_avaliacao
            JOIN Cliente cl ON a.id_cliente = cl.id_cliente
            JOIN Sessao s ON a.id_sessao = s.id_sessao
            JOIN Sala sa ON s.id_sala = sa.id_sala
            JOIN Cinema ci ON sa.id_cinema = ci.id_cinema
            JOIN Filme f ON s.id_filme = f.id_filme
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();
        aplicarFiltros(sql, params, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);

        sql.append(" GROUP BY c.nome_criterio");
        return executarConsultaJson(conn, sql.toString(), params, new String[]{"nome_criterio", "media"});
    }

    private String consultarAvaliacoesPorCinema(Connection conn, LocalDate inicio, LocalDate fim,
                                                String faixaEtaria, Long idCinema, String tipoSala,
                                                String sentimento, Long idFilme) throws Exception {
        StringBuilder sql = new StringBuilder("""
            SELECT ci.nome,
                   COUNT(a.id_avaliacao) AS total_avaliacoes
            FROM Avaliacao a
            JOIN Sessao s ON a.id_sessao = s.id_sessao
            JOIN Sala sa ON s.id_sala = sa.id_sala
            JOIN Cinema ci ON sa.id_cinema = ci.id_cinema
            JOIN Cliente cl ON a.id_cliente = cl.id_cliente
            JOIN Filme f ON s.id_filme = f.id_filme
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();
        aplicarFiltros(sql, params, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);

        sql.append(" GROUP BY ci.nome");
        return executarConsultaJson(conn, sql.toString(), params, new String[]{"nome", "total_avaliacoes"});
    }

    private String consultarEvolucaoTemporal(Connection conn, LocalDate inicio, LocalDate fim,
                                             String faixaEtaria, Long idCinema, String tipoSala,
                                             String sentimento, Long idFilme) throws Exception {
        StringBuilder sql = new StringBuilder("""
            SELECT 
                DATE(a.data_avaliacao) AS data,
                AVG(a.nota_geral) AS media_dia,
                COUNT(a.id_avaliacao) AS total_avaliacoes
            FROM Avaliacao a
            JOIN Cliente cl ON a.id_cliente = cl.id_cliente
            JOIN Sessao s ON a.id_sessao = s.id_sessao
            JOIN Sala sa ON s.id_sala = sa.id_sala
            JOIN Cinema ci ON sa.id_cinema = ci.id_cinema
            JOIN Filme f ON s.id_filme = f.id_filme
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();
        aplicarFiltros(sql, params, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);

        sql.append(" GROUP BY DATE(a.data_avaliacao) ORDER BY data");
        return executarConsultaJson(conn, sql.toString(), params, new String[]{"data", "media_dia", "total_avaliacoes"});
    }

    private String consultarNotasPorSala(Connection conn, LocalDate inicio, LocalDate fim,
                                         String faixaEtaria, Long idCinema, String tipoSala,
                                         String sentimento, Long idFilme) throws Exception {
        StringBuilder sql = new StringBuilder("""
            SELECT sa.tipo_sala,
                   AVG(a.nota_geral) AS media
            FROM Avaliacao a
            JOIN Sessao s ON a.id_sessao = s.id_sessao
            JOIN Sala sa ON s.id_sala = sa.id_sala
            JOIN Cinema ci ON sa.id_cinema = ci.id_cinema
            JOIN Cliente cl ON a.id_cliente = cl.id_cliente
            JOIN Filme f ON s.id_filme = f.id_filme
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();
        aplicarFiltros(sql, params, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);

        sql.append(" GROUP BY sa.tipo_sala");
        return executarConsultaJson(conn, sql.toString(), params, new String[]{"tipo_sala", "media"});
    }

    private String consultarSentimentos(Connection conn, LocalDate inicio, LocalDate fim,
                                        String faixaEtaria, Long idCinema, String tipoSala,
                                        String sentimento, Long idFilme) throws Exception {
        StringBuilder sql = new StringBuilder("""
            SELECT a.sentimento_comentario,
                   COUNT(a.id_avaliacao) AS total
            FROM Avaliacao a
            JOIN Sessao s ON a.id_sessao = s.id_sessao
            JOIN Sala sa ON s.id_sala = sa.id_sala
            JOIN Cinema ci ON sa.id_cinema = ci.id_cinema
            JOIN Cliente cl ON a.id_cliente = cl.id_cliente
            JOIN Filme f ON s.id_filme = f.id_filme
            WHERE a.sentimento_comentario IS NOT NULL AND 1=1
        """);

        List<Object> params = new ArrayList<>();
        aplicarFiltros(sql, params, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);

        sql.append(" GROUP BY a.sentimento_comentario");
        return executarConsultaJson(conn, sql.toString(), params, new String[]{"sentimento_comentario", "total"});
    }

    private String consultarAvaliacoesPorFilme(Connection conn, LocalDate inicio, LocalDate fim,
                                               String faixaEtaria, Long idCinema, String tipoSala,
                                               String sentimento, Long idFilme) throws Exception {
        StringBuilder sql = new StringBuilder("""
            SELECT f.titulo,
                   AVG(a.nota_geral) AS media_geral,
                   COUNT(a.id_avaliacao) AS total_avaliacoes
            FROM Avaliacao a
            JOIN Sessao s ON a.id_sessao = s.id_sessao
            JOIN Sala sa ON s.id_sala = sa.id_sala
            JOIN Cinema ci ON sa.id_cinema = ci.id_cinema
            JOIN Cliente cl ON a.id_cliente = cl.id_cliente
            JOIN Filme f ON s.id_filme = f.id_filme
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();
        aplicarFiltros(sql, params, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);

        sql.append(" GROUP BY f.titulo");
        return executarConsultaJson(conn, sql.toString(), params, new String[]{"titulo", "media_geral", "total_avaliacoes"});
    }

    private String consultarAvaliacoesPorFaixa(Connection conn, LocalDate inicio, LocalDate fim,
                                               String faixaEtaria, Long idCinema, String tipoSala,
                                               String sentimento, Long idFilme) throws Exception {
        StringBuilder sql = new StringBuilder("""
            SELECT
                CASE
                    WHEN EXTRACT(YEAR FROM AGE(CURRENT_DATE, cl.data_nascimento)) BETWEEN 18 AND 25 THEN '18-25'
                    WHEN EXTRACT(YEAR FROM AGE(CURRENT_DATE, cl.data_nascimento)) BETWEEN 26 AND 35 THEN '26-35'
                    WHEN EXTRACT(YEAR FROM AGE(CURRENT_DATE, cl.data_nascimento)) BETWEEN 36 AND 45 THEN '36-45'
                    WHEN EXTRACT(YEAR FROM AGE(CURRENT_DATE, cl.data_nascimento)) > 45 THEN '>45'
                    ELSE 'MENOR_IDADE_OU_OUTRO'
                END AS faixa_etaria,
                AVG(a.nota_geral) AS media_geral,
                COUNT(a.id_avaliacao) AS total_avaliacoes
            FROM Avaliacao a
            JOIN Cliente cl ON a.id_cliente = cl.id_cliente
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
        return executarConsultaJson(conn, sql.toString(), params, new String[]{"faixa_etaria", "media_geral", "total_avaliacoes"});
    }

    private String consultarAvaliacoesPorHora(Connection conn, LocalDate inicio, LocalDate fim,
                                              String faixaEtaria, Long idCinema, String tipoSala,
                                              String sentimento, Long idFilme) throws Exception {
        StringBuilder sql = new StringBuilder("""
            SELECT EXTRACT(HOUR FROM a.data_avaliacao) AS hora_dia,
                   AVG(a.nota_geral) AS media_hora,
                   COUNT(a.id_avaliacao) AS total_avaliacoes
            FROM Avaliacao a
            JOIN Cliente cl ON a.id_cliente = cl.id_cliente
            JOIN Sessao s ON a.id_sessao = s.id_sessao
            JOIN Sala sa ON s.id_sala = sa.id_sala
            JOIN Cinema ci ON sa.id_cinema = ci.id_cinema
            JOIN Filme f ON s.id_filme = f.id_filme
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();
        aplicarFiltros(sql, params, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);

        sql.append(" GROUP BY hora_dia ORDER BY hora_dia");
        return executarConsultaJson(conn, sql.toString(), params, new String[]{"hora_dia", "media_hora", "total_avaliacoes"});
    }

    private String executarConsultaJson(Connection conn, String sql, List<Object> params, String[] colunas) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();

            StringBuilder arr = new StringBuilder("[");
            boolean first = true;

            while (rs.next()) {
                if (!first) arr.append(",");
                arr.append("{");

                for (int j = 0; j < colunas.length; j++) {
                    String col = colunas[j];
                    Object val = rs.getObject(col);

                    arr.append("\"").append(col).append("\":");

                    if (val == null) {
                        arr.append("null");
                    } else if (val instanceof Number) {
                        arr.append(val);
                    } else if (val instanceof java.sql.Date || val instanceof java.sql.Timestamp) {
                        arr.append("\"").append(val.toString()).append("\"");
                    } else {
                        String escaped = val.toString()
                                .replace("\\", "\\\\")
                                .replace("\"", "\\\"")
                                .replace("\n", "\\n")
                                .replace("\r", "\\r")
                                .replace("\t", "\\t");
                        arr.append("\"").append(escaped).append("\"");
                    }

                    if (j < colunas.length - 1) arr.append(",");
                }
                arr.append("}");
                first = false;
            }
            arr.append("]");
            return arr.toString();
        }
    }
}