package com.lk.common.mqtt.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * 扫描MessagingGateway注解 生成代理类
 */
@Slf4j
@IntegrationComponentScan(basePackages = "com.lk.common.mqtt")
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(MqttConfig.class)
public class IotMqttProducerConfig {

    @Bean
    public MqttPahoClientFactory mqttClientFactory(MqttConfig mqttConfig) {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setServerURIs(new String[]{mqttConfig.getServers()});
        mqttConnectOptions.setUserName(mqttConfig.getUsername());
        mqttConnectOptions.setPassword(mqttConfig.getPassword().toCharArray());
        mqttConnectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions);
        return factory;
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "iotMqttInputChannel")
    public MessageHandler mqttOutbound(MqttPahoClientFactory mqttPahoClientFactory, MqttConfig mqttConfig) {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(mqttConfig.getClientId(), mqttPahoClientFactory);
        //设置默认的qos级别
        messageHandler.setDefaultQos(1);
        //保留标志的默认值。如果没有mqtt_retained找到标题，则使用它。如果提供了自定义，则不使用它converter。这里不启用
        messageHandler.setDefaultRetained(false);
        //设置发布的主题
        messageHandler.setDefaultTopic(mqttConfig.getDefaultTopic());
        //当 时true，调用者不会阻塞。相反，它在发送消息时等待传递确认。默认值为false（在确认交付之前发送阻止）。
        messageHandler.setAsync(false);
        //当 async 和 async-events 都为 true 时，会发出 MqttMessageSentEvent（请参阅事件）。它包含消息、主题、客户端库生成的messageId、clientId和clientInstance（每次连接客户端时递增）。当客户端库确认交付时，会发出 MqttMessageDeliveredEvent。它包含 messageId、clientId 和 clientInstance，使传递与发送相关联。任何 ApplicationListener 或事件入站通道适配器都可以接收这些事件。请注意，有可能在 MqttMessageSentEvent 之前接收到 MqttMessageDeliveredEvent。默认值为false。
        messageHandler.setAsyncEvents(false);
        return messageHandler;
    }

}
