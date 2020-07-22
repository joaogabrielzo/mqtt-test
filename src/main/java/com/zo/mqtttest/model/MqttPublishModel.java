package com.zo.mqtttest.model;

import lombok.Data;


@Data
public class MqttPublishModel {

    private String topic;
    private String message;
    private Boolean retained;
    private Integer qos;

}
