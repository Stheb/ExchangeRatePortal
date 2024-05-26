package org.example.exchangerateportal.configuration;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XmlConfiguration {

    @Bean
    public XmlMapper xmlMapper() {
        return new XmlMapper();
    }
}
