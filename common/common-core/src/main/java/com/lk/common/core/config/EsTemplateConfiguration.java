package com.lk.common.core.config;

import lombok.SneakyThrows;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.http.HttpHeaders;

import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.time.Duration;

@AutoConfiguration
public class EsTemplateConfiguration extends AbstractElasticsearchConfiguration {

    @SneakyThrows
    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {

        HttpHeaders compatibilityHeaders = new HttpHeaders();
        compatibilityHeaders.add("Accept", "application/vnd.elasticsearch+json;compatible-with=7");
        compatibilityHeaders.add("Content-Type", "application/vnd.elasticsearch+json;"
                + "compatible-with=7");

        ClassPathResource classPathResource = new ClassPathResource("http_ca.crt");
        CertificateFactory factory =
                CertificateFactory.getInstance("X.509");
        Certificate trustedCa;
        try (InputStream is = classPathResource.getInputStream()) {
            trustedCa = factory.generateCertificate(is);
        }
        KeyStore trustStore = KeyStore.getInstance("pkcs12");
        trustStore.load(null, null);
        trustStore.setCertificateEntry("ca", trustedCa);
        SSLContextBuilder sslContextBuilder = SSLContexts.custom()
                .loadTrustMaterial(trustStore, null);
        SSLContext sslContext = sslContextBuilder.build();

        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:9200")
                .usingSsl(sslContext)
                .withConnectTimeout(Duration.ofSeconds(5))
                .withSocketTimeout(Duration.ofSeconds(3))
                .withBasicAuth("elastic", "xA123456")
                // this variant for imperative code
                .withDefaultHeaders(compatibilityHeaders)
                // this variant for reactive code
//                .withHeaders(() -> compatibilityHeaders)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

}