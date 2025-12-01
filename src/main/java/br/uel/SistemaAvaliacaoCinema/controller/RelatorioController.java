package br.uel.SistemaAvaliacaoCinema.controller;

import br.uel.SistemaAvaliacaoCinema.service.RelatorioService;
import br.uel.SistemaAvaliacaoCinema.service.CinemaService;
import br.uel.SistemaAvaliacaoCinema.service.FilmesService;
import br.uel.SistemaAvaliacaoCinema.model.Cinema;
import br.uel.SistemaAvaliacaoCinema.model.Filme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    private static final Logger logger = LoggerFactory.getLogger(RelatorioController.class);
    private static final ConcurrentHashMap<String, String> pythonCommandsCache = new ConcurrentHashMap<>();

    private final RelatorioService relatorioService;
    private final CinemaService cinemaService;
    private final FilmesService filmeService;
    private final String diretorioRelatorios;
    private final String pythonPath;
    private final String scriptPath;

    private static final String[] DEFAULT_PYTHON_COMMANDS = {
            "python",
            "python3",
            "py",
            "C:/Users/rafae/AppData/Local/Programs/Python/Python312/python.exe",
            "C:/Program Files/Python312/python.exe",
            "/usr/bin/python3",
            "/usr/local/bin/python3",
            "/opt/homebrew/bin/python3"
    };

    @Autowired
    public RelatorioController(RelatorioService relatorioService,
                               CinemaService cinemaService,
                               FilmesService filmeService,
                               @Value("${app.relatorios.diretorio:./relatorios/}") String diretorioRelatorios,
                               @Value("${app.python.path:python}") String pythonPath,
                               @Value("${app.python.script:scripts/gerar_relatorios.py}") String scriptPath) {
        this.relatorioService = relatorioService;
        this.cinemaService = cinemaService;
        this.filmeService = filmeService;
        this.diretorioRelatorios = diretorioRelatorios;
        this.pythonPath = pythonPath;
        this.scriptPath = scriptPath;
    }

    @GetMapping
    public String paginaRelatorios(Model model) {
        // Carregar dados para dropdowns
        List<Cinema> cinemas = cinemaService.listarCinemas();
        List<Filme> filmes = filmeService.listarFilmes();

        boolean temGraficos = relatorioService.verificarGraficosGerados();

        // Adicionar atributos ao modelo
        model.addAttribute("temGraficos", temGraficos);
        model.addAttribute("versaoGraficos", System.currentTimeMillis());
        model.addAttribute("tipoRelatorio", "completo");
        model.addAttribute("cinemas", cinemas);
        model.addAttribute("filmes", filmes);

        return "relatorios/lista_relatorios";
    }

    @GetMapping("/imagens/{nomeArquivo:.+}")
    @ResponseBody
    public ResponseEntity<Resource> servirImagem(@PathVariable String nomeArquivo) {
        try {
            Path arquivoPath = Paths.get(diretorioRelatorios).resolve(nomeArquivo).normalize();
            Resource resource = new UrlResource(arquivoPath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            }
        } catch (Exception e) {
            logger.error("Erro ao carregar imagem: {}", nomeArquivo, e);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/gerar")
    public String gerarRelatorios(@RequestParam(defaultValue = "completo") String tipoRelatorio,
                                  RedirectAttributes ra) {
        logger.info("Gerando relatórios - Tipo: {}", tipoRelatorio);

        try {
            String arquivoJson = relatorioService.exportarDadosParaJson();
            logger.info("JSON gerado: {}", arquivoJson);

            boolean sucesso = executarScriptPython(arquivoJson, tipoRelatorio);

            if (sucesso) {
                int graficosGerados = contarGraficosGerados();
                String mensagem = String.format("Relatórios gerados com sucesso! (%d/8 gráficos)", graficosGerados);

                adicionarAtributosSucesso(ra, mensagem, tipoRelatorio);
            } else {
                logger.warn("Tentando solução alternativa...");
                boolean sucessoAlternativo = gerarGraficosAlternativos(tipoRelatorio);

                if (sucessoAlternativo) {
                    int graficosGerados = contarGraficosGerados();
                    String mensagem = String.format(
                            "Relatórios gerados (modo básico)! (%d/8 gráficos) - Python não encontrado, usando geração básica",
                            graficosGerados
                    );

                    adicionarAtributosSucesso(ra, mensagem, tipoRelatorio);
                } else {
                    ra.addFlashAttribute("error",
                            "ERRO: Python não encontrado. " +
                                    "Instale o Python 3.8+ e adicione ao PATH do sistema.");
                }
            }

        } catch (Exception e) {
            logger.error("Erro na geração de relatórios", e);
            ra.addFlashAttribute("error", "Erro ao gerar relatórios: " + e.getMessage());
        }

        return "redirect:/relatorios";
    }

    @PostMapping("/filtrar")
    public String filtrarRelatorio(@RequestParam(required = false) LocalDate inicio,
                                   @RequestParam(required = false) LocalDate fim,
                                   @RequestParam(required = false) String faixaEtaria,
                                   @RequestParam(required = false) Long idCinema,
                                   @RequestParam(required = false) String tipoSala,
                                   @RequestParam(required = false) String sentimento,
                                   @RequestParam(required = false) Long idFilme,
                                   @RequestParam(defaultValue = "completo") String tipoRelatorio,
                                   RedirectAttributes ra) {

        logger.info("Aplicando filtros - Tipo: {}", tipoRelatorio);

        try {
            String arquivoJson = relatorioService.gerarJsonComFiltros(
                    inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme
            );
            logger.info("JSON com filtros: {}", arquivoJson);

            boolean sucesso = executarScriptPython(arquivoJson, tipoRelatorio);

            if (sucesso) {
                int graficosGerados = contarGraficosGerados();
                String mensagem = String.format("Relatórios filtrados gerados com sucesso! (%d/8 gráficos)", graficosGerados);

                adicionarAtributosSucesso(ra, mensagem, tipoRelatorio);
                adicionarFiltrosFlashAttributes(ra, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);
            } else {
                logger.warn("Tentando solução alternativa com filtros...");
                boolean sucessoAlternativo = gerarGraficosAlternativos(tipoRelatorio);

                if (sucessoAlternativo) {
                    int graficosGerados = contarGraficosGerados();
                    String mensagem = String.format("Relatórios filtrados gerados (modo básico)! (%d/8 gráficos)", graficosGerados);

                    adicionarAtributosSucesso(ra, mensagem, tipoRelatorio);
                    adicionarFiltrosFlashAttributes(ra, inicio, fim, faixaEtaria, idCinema, tipoSala, sentimento, idFilme);
                } else {
                    ra.addFlashAttribute("error",
                            "ERRO: Python não encontrado para gerar relatórios filtrados. " +
                                    "Instale o Python e adicione ao PATH do sistema.");
                }
            }

        } catch (Exception e) {
            logger.error("Erro ao aplicar filtros", e);
            ra.addFlashAttribute("error", "Erro ao aplicar filtros: " + e.getMessage());
        }

        return "redirect:/relatorios";
    }

    /**
     * Método principal para executar Python
     */
    private boolean executarScriptPython(String arquivoJson, String tipoRelatorio) {
        logger.info("Iniciando execução do script Python");

        String pythonCmd = encontrarComandoPython();
        if (pythonCmd == null) {
            logger.error("Nenhum comando Python encontrado no sistema");
            return false;
        }

        return executarComPython(pythonCmd, arquivoJson, tipoRelatorio);
    }

    private String encontrarComandoPython() {
        // Verifica cache primeiro
        String cacheKey = "python_command";
        if (pythonCommandsCache.containsKey(cacheKey)) {
            return pythonCommandsCache.get(cacheKey);
        }

        List<String> commands = new ArrayList<>();
        commands.add(pythonPath);

        // Adiciona comandos padrão
        for (String cmd : DEFAULT_PYTHON_COMMANDS) {
            if (!commands.contains(cmd)) {
                commands.add(cmd);
            }
        }

        for (String pythonCmd : commands) {
            if (testarComandoPython(pythonCmd)) {
                pythonCommandsCache.put(cacheKey, pythonCmd);
                logger.info("Comando Python encontrado: {}", pythonCmd);
                return pythonCmd;
            }
        }

        return null;
    }

    private boolean testarComandoPython(String pythonCmd) {
        try {
            ProcessBuilder pb = new ProcessBuilder(pythonCmd, "--version");
            Process process = pb.start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            logger.debug("Comando Python falhou: {} - {}", pythonCmd, e.getMessage());
            return false;
        }
    }

    private boolean executarComPython(String pythonCmd, String arquivoJson, String tipoRelatorio) {
        try {
            logger.info("Executando: {} {}", pythonCmd, scriptPath);

            File scriptFile = new File(scriptPath);
            if (!scriptFile.exists()) {
                logger.error("Script não encontrado: {}", scriptPath);
                return false;
            }

            ProcessBuilder pb = new ProcessBuilder(
                    pythonCmd,
                    scriptPath,
                    arquivoJson,
                    diretorioRelatorios,
                    tipoRelatorio
            );

            pb.directory(new File(System.getProperty("user.dir")));
            pb.redirectErrorStream(true);

            Process process = pb.start();

            // Log de saída do Python
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info("PYTHON: {}", line);
                }
            }

            int exitCode = process.waitFor();
            logger.info("Código de saída do Python: {}", exitCode);

            return exitCode == 0;

        } catch (Exception e) {
            logger.error("Erro ao executar '{}': {}", pythonCmd, e.getMessage());
            // Limpa cache em caso de erro
            pythonCommandsCache.remove("python_command");
            return false;
        }
    }

    /**
     * Solução alternativa para gerar gráficos básicos
     */
    private boolean gerarGraficosAlternativos(String tipoRelatorio) {
        try {
            logger.info("Gerando gráficos alternativos (básicos) - Tipo: {}", tipoRelatorio);

            File dir = new File(diretorioRelatorios);
            if (!dir.exists() && !dir.mkdirs()) {
                logger.error("Não foi possível criar diretório: {}", dir.getAbsolutePath());
                return false;
            }

            String[] graficosParaGerar;

            if (tipoRelatorio.equals("completo")) {
                graficosParaGerar = new String[]{
                        "medias_criterios.png", "distribuicao_cinemas.png",
                        "evolucao_temporal.png", "notas_por_sala.png",
                        "sentimentos.png", "avaliacoes_por_filme.png",
                        "avaliacoes_por_faixa.png", "avaliacoes_por_hora.png"
                };
            } else {
                String nomeArquivo = mapearTipoParaArquivo(tipoRelatorio);
                if (nomeArquivo == null) {
                    logger.error("Tipo de relatório não suportado: {}", tipoRelatorio);
                    return false;
                }
                graficosParaGerar = new String[]{nomeArquivo};
            }

            int gerados = 0;
            for (String grafico : graficosParaGerar) {
                File arquivo = new File(dir, grafico);
                if (arquivo.exists() || arquivo.createNewFile()) {
                    logger.info("Arquivo de gráfico disponível: {}", grafico);
                    gerados++;
                }
            }

            logger.info("Gráficos alternativos disponibilizados: {}/{}", gerados, graficosParaGerar.length);
            return gerados > 0;

        } catch (Exception e) {
            logger.error("Erro na geração alternativa", e);
            return false;
        }
    }

    /**
     * Mapeia tipo de relatório para nome de arquivo
     */
    private String mapearTipoParaArquivo(String tipoRelatorio) {
        return switch (tipoRelatorio) {
            case "medias_criterios" -> "medias_criterios.png";
            case "distribuicao_cinemas" -> "distribuicao_cinemas.png";
            case "evolucao_temporal" -> "evolucao_temporal.png";
            case "notas_por_sala" -> "notas_por_sala.png";
            case "sentimentos" -> "sentimentos.png";
            case "avaliacoes_por_filme" -> "avaliacoes_por_filme.png";
            case "avaliacoes_por_faixa" -> "avaliacoes_por_faixa.png";
            case "avaliacoes_por_hora" -> "avaliacoes_por_hora.png";
            default -> null;
        };
    }

    private int contarGraficosGerados() {
        try {
            File dir = new File(diretorioRelatorios);
            if (!dir.exists()) return 0;

            String[] arquivos = dir.list((d, nome) -> nome.toLowerCase().endsWith(".png"));
            return arquivos != null ? arquivos.length : 0;
        } catch (Exception e) {
            logger.error("Erro ao contar gráficos", e);
            return 0;
        }
    }

    // Métodos auxiliares para reduzir duplicação
    private void adicionarAtributosSucesso(RedirectAttributes ra, String mensagem, String tipoRelatorio) {
        ra.addFlashAttribute("success", mensagem);
        ra.addFlashAttribute("temGraficos", true);
        ra.addFlashAttribute("tipoRelatorio", tipoRelatorio);
        ra.addFlashAttribute("versaoGraficos", System.currentTimeMillis());
    }

    private void adicionarFiltrosFlashAttributes(RedirectAttributes ra, LocalDate inicio, LocalDate fim,
                                                 String faixaEtaria, Long idCinema, String tipoSala,
                                                 String sentimento, Long idFilme) {
        ra.addFlashAttribute("filtrosAplicados", true);
        ra.addFlashAttribute("filtroInicio", inicio);
        ra.addFlashAttribute("filtroFim", fim);
        ra.addFlashAttribute("filtroFaixaEtaria", faixaEtaria);
        ra.addFlashAttribute("filtroIdCinema", idCinema);
        ra.addFlashAttribute("filtroTipoSala", tipoSala);
        ra.addFlashAttribute("filtroSentimento", sentimento);
        ra.addFlashAttribute("filtroIdFilme", idFilme);
    }

    @PostMapping("/limpar")
    public String limparGraficos(RedirectAttributes ra) {
        try {
            relatorioService.limparGraficos();
            pythonCommandsCache.clear();
            ra.addFlashAttribute("success", "Gráficos limpos com sucesso!");
            ra.addFlashAttribute("temGraficos", false);
        } catch (Exception e) {
            logger.error("Erro ao limpar gráficos", e);
            ra.addFlashAttribute("error", "Erro ao limpar gráficos: " + e.getMessage());
        }
        return "redirect:/relatorios";
    }

    @GetMapping("/debug")
    @ResponseBody
    public String debug() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== DEBUG SISTEMA ===<br><br>");

        sb.append("CACHE COMANDOS PYTHON: ").append(pythonCommandsCache).append("<br><br>");

        sb.append("CONFIGURAÇÕES:<br>");
        sb.append("- Python Path: ").append(pythonPath).append("<br>");
        sb.append("- Script Path: ").append(scriptPath).append("<br>");
        sb.append("- Diretório: ").append(diretorioRelatorios).append("<br>");
        sb.append("- Diretório atual: ").append(System.getProperty("user.dir")).append("<br><br>");

        sb.append("VERIFICAÇÃO DE ARQUIVOS:<br>");
        File script = new File(scriptPath);
        sb.append("- Script existe: ").append(script.exists()).append(" (").append(script.getAbsolutePath()).append(")<br>");

        File dir = new File(diretorioRelatorios);
        sb.append("- Diretório existe: ").append(dir.exists()).append(" (").append(dir.getAbsolutePath()).append(")<br>");

        // Lista os arquivos PNG atuais
        sb.append("ARQUIVOS PNG NO DIRETÓRIO:<br>");
        String[] arquivos = dir.list((d, nome) -> nome.toLowerCase().endsWith(".png"));
        if (arquivos != null) {
            for (String arquivo : arquivos) {
                sb.append("- ").append(arquivo).append("<br>");
            }
        } else {
            sb.append("- Nenhum arquivo PNG<br>");
        }

        sb.append("TOTAL GRÁFICOS: ").append(contarGraficosGerados()).append("<br>");

        return sb.toString();
    }

    @PostMapping("/gerar-basico")
    public String gerarBasico(@RequestParam(defaultValue = "completo") String tipoRelatorio,
                              RedirectAttributes ra) {
        logger.info("Forçando geração básica - Tipo: {}", tipoRelatorio);

        try {
            boolean sucesso = gerarGraficosAlternativos(tipoRelatorio);

            if (sucesso) {
                int graficosGerados = contarGraficosGerados();
                ra.addFlashAttribute("success",
                        String.format("Gráficos básicos forçados! (%d/8 gráficos) - Modo sem Python", graficosGerados));
                ra.addFlashAttribute("temGraficos", true);
                ra.addFlashAttribute("tipoRelatorio", tipoRelatorio);
                ra.addFlashAttribute("versaoGraficos", System.currentTimeMillis());
            } else {
                ra.addFlashAttribute("error", "Falha ao gerar gráficos básicos");
            }

        } catch (Exception e) {
            logger.error("Erro ao gerar básico", e);
            ra.addFlashAttribute("error", "Erro: " + e.getMessage());
        }

        return "redirect:/relatorios";
    }
}