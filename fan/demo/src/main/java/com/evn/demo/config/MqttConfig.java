package com.evn.demo.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

@Configuration
public class MqttConfig {

    @Value("tcp://154.21.200.171")
    private String brokerUrl;


    //上传的项目
    @Value("hi3861_fan_1")
    private String clientId;
    
    public String getBrokerUrl() {
        if (brokerUrl == null || brokerUrl.isEmpty()) {
            throw new IllegalStateException("MQTT broker URL must be configured");
        }
        if (!brokerUrl.startsWith("tcp://") && !brokerUrl.startsWith("ssl://")) {
            throw new IllegalStateException("Invalid MQTT broker URL protocol");
        }
        return brokerUrl;
    }
    
    public String getClientId() {
        if (clientId == null || clientId.isEmpty()) {
            throw new IllegalStateException("MQTT client ID must be configured");
        }
        return clientId;
    }

    @Value("admin")
    private String username;

    @Value("jyb20040302")
    private String password;

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{brokerUrl});
        options.setCleanSession(true);
        options.setAutomaticReconnect(true);
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(60);

        if (username != null && !username.isEmpty()) {
            options.setUserName(username);
            options.setPassword(password.toCharArray());
        }

        return options;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions());
        return factory;
    }


}