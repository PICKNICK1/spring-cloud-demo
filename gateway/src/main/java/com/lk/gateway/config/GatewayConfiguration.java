package com.lk.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lk.gateway.filter.PasswordDecoderFilter;
import com.lk.gateway.filter.RequestGlobalFilter;
import com.lk.gateway.filter.SwaggerBasicGatewayFilter;
import com.lk.gateway.filter.ValidateCodeGatewayFilter;
import com.lk.gateway.handler.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 网关配置
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(GatewayConfigProperties.class)
public class GatewayConfiguration {

    @Bean
    public PasswordDecoderFilter passwordDecoderFilter(GatewayConfigProperties configProperties) {
        return new PasswordDecoderFilter(configProperties);
    }

    @Bean
    public RequestGlobalFilter requestGlobalFilter() {
        return new RequestGlobalFilter();
    }

    @Bean
    @ConditionalOnProperty(name = "swagger.basic.enabled")
    public SwaggerBasicGatewayFilter swaggerBasicGatewayFilter(
            SpringDocConfiguration.SwaggerDocProperties swaggerProperties) {
        return new SwaggerBasicGatewayFilter(swaggerProperties);
    }

    @Bean
    public ValidateCodeGatewayFilter validateCodeGatewayFilter(GatewayConfigProperties configProperties,
                                                               ObjectMapper objectMapper, RedisTemplate redisTemplate) {
        return new ValidateCodeGatewayFilter(configProperties, objectMapper, redisTemplate);
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler(ObjectMapper objectMapper) {
        return new GlobalExceptionHandler(objectMapper);
    }


}
