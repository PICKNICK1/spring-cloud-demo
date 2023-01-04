package com.lk.common.mqtt.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mqtt.config")
public class MqttConfig {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
    /**
     * 服务地址
     */
    private String servers;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 默认主题
     */
    private String defaultTopic;
}
