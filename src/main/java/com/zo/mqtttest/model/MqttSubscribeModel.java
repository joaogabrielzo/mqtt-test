package com.zo.mqtttest.model;

import lombok.Data;

@Data
public class MqttSubscribeModel {

    private String message;
    private Integer qos;
    private Integer id;
}
