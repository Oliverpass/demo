package com.evn.demo.config;
import com.evn.demo.service.SensorDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@Slf4j
@Configuration
public class MqttSubscriber {

    @Autowired
    private MqttPahoClientFactory mqttClientFactory;

    //订阅的项目
    @Value("fan,fan_1")
    private String topic;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SensorDataService sensorDataService;

    @Bean
    public MqttPahoMessageDrivenChannelAdapter mqttInbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter("mqtt-printer-" + System.currentTimeMillis()+"-in", mqttClientFactory, topic.split(",")[0]);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        return adapter;
    }

    @Bean
    public IntegrationFlow mqttInFlow() {
        return IntegrationFlows.from(mqttInbound())
                .handle(message -> {
                    String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
                    String payload = (String) message.getPayload();
                    // 打印原始消息
                    log.info("\n=== 收到MQTT消息 ===");
                    log.info("主题: {}", topic);
                    log.info("原始数据: {}", payload);
                    log.info("=== 消息结束 ===\n");

                    //将硬件信息上传到数据库中
                    /*sensorDataService.saveSensorData(topic, payload);*/


                })
                .get();
    }


    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler =
                new MqttPahoMessageHandler("mqtt-printer-" + System.currentTimeMillis() + "-out", mqttClientFactory);
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(topic.split(",")[1]);
        return messageHandler;
    }

}