package com.evn.demo.controller;

import com.evn.demo.service.MqttService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {
    
    @Autowired
    private MqttService mqttService;

    //将硬件上传上来的信息传入数据库中
    @GetMapping("/send")
    public String sendMessage(@RequestParam String topic, @RequestParam String message) throws MqttException {
        mqttService.sendMessage(topic, message);
        return "Message sent to topic " + topic;
    }

    //控制风扇硬件
    @PostMapping("/control-fan")
    public String controlFan(@RequestBody Map<String, Integer> request) throws MqttException {
        int speed = request.get("speed");
        mqttService.sendMessage("fan_1", String.valueOf(speed));
        return "风扇挡位已设置为: " + speed;
    }
}