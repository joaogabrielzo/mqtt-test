package com.zo.mqtttest.controller;

import com.zo.mqtttest.config.Mqtt;
import com.zo.mqtttest.model.*;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api/mqtt")
public class MqttController {

    @PostMapping("publish")
    public ResponseEntity<Object> publishMessage(@RequestBody MqttPublishModel message, BindingResult bindingResult)
            throws MqttException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        MqttMessage msg = new MqttMessage(message.getMessage().getBytes());
        msg.setQos(message.getQos());
        msg.setRetained(message.getRetained());

        Mqtt.getInstance().publish(message.getTopic(), msg);

        return ResponseEntity.created(URI.create("localhost:1883/api/mqtt/subscribe/" + message.getTopic())).build();
    }

    @GetMapping("subscribe")
    public List<MqttSubscribeModel> subscribeChannel(@RequestParam(value = "topic") String topic)
            throws MqttException, InterruptedException {
        List<MqttSubscribeModel> messages = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Mqtt.getInstance().subscribeWithResponse(topic,
                (s, mqttMessage) -> {
                    MqttSubscribeModel mqttSubscribeModel = new MqttSubscribeModel();
                    mqttSubscribeModel.setId(mqttMessage.getId());
                    mqttSubscribeModel.setMessage(new String(mqttMessage.getPayload()));
                    mqttSubscribeModel.setQos(mqttMessage.getQos());
                    messages.add(mqttSubscribeModel);
                    countDownLatch.countDown();
                });

        countDownLatch.await(2000, TimeUnit.MILLISECONDS);

        return messages;
    }

}
