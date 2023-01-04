package com.lk.common.mqtt.controller;

import com.lk.common.core.util.R;
import com.lk.common.mqtt.produce.IotMqttGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/mqtt")
public class IotMqttController {
    @Autowired
    private IotMqttGateway mqttGateway;

    @RequestMapping("/sendMessage")
    @ResponseBody
    public R sendMqtt(@RequestParam(value = "topic") String topic, @RequestParam(value = "message") String message) {
        mqttGateway.sendToMqtt(topic,message);
        return R.ok();
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }
}
