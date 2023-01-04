package com.lk.common.mqtt.produce;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway(defaultRequestChannel = "iotMqttInputChannel")
public interface IotMqttGateway {

    /**
     * 发送到mqtt
     *
     * @param payload 有效载荷
     */
    void sendToMqtt(String payload);

    /**
     * 发送到mqtt
     *
     * @param topic   主题
     * @param payload 消息内容
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String payload);

    /**
     * 发送到mqtt
     *
     * @param topic   主题
     * @param qos     qos
     * @param payload 消息内容
     */
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);
}
