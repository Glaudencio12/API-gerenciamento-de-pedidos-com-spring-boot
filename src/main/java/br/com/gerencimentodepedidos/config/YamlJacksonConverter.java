package br.com.gerencimentodepedidos.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

public class YamlJacksonConverter extends AbstractJackson2HttpMessageConverter {

    public YamlJacksonConverter() {
        super(
                new YAMLMapper()
                        .setSerializationInclusion(JsonInclude.Include.NON_NULL),
                MediaType.valueOf("application/yml")
        );
    }
}
