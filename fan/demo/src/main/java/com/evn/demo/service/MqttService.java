package com.evn.demo.service;

import com.evn.demo.config.MqttConfig;
import lombok.Value;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

@Service
public class MqttService {
    private MqttClient mqttClient;
    
    private final String mqttBrokerUrl;
    private final String clientId;
    
    public MqttService(MqttConfig mqttConfig) throws MqttException {
        this.mqttBrokerUrl = mqttConfig.getBrokerUrl();
        this.clientId = mqttConfig.getClientId();
        this.mqttClient = new MqttClient(mqttBrokerUrl, clientId);
        
        this.mqttClient.connect();
    }
    
    public void sendMessage(String topic, String payload) throws MqttException {
        MqttMessage message = new MqttMessage();
        message.setPayload(payload.getBytes());
        mqttClient.publish(topic, message);
        System.out.println("MQTT消息已发送 - 主题: " + topic + ", 内容: " + payload);
    }
    
    
}