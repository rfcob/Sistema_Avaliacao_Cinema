package br.uel.SistemaAvaliacaoCinema.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.relatorios.diretorio:./relatorios/}")
    private String diretorioRelatorios;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Certificar que o diretório termina com /
        String path = diretorioRelatorios;
        if (!path.endsWith("/")) {
            path = path + "/";
        }

        registry.addResourceHandler("/relatorios/imagens/**")
                .addResourceLocations("file:" + path);

        // Adicionar também handler para arquivos estáticos comuns
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/public/");
    }
}