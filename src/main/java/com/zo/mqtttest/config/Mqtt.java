package com.zo.mqtttest.config;

import org.eclipse.paho.client.mqttv3.*;

public class Mqtt {

    private static final String MQTT_PUBLISHER_ID = "test";
    private static final String MQTT_SERVER_ADDRESS = "tcp://127.0.0.1:1883";
    private static IMqttClient instance;

    public static IMqttClient getInstance() {
        try {
            if (instance == null) {
                instance = new MqttClient(MQTT_SERVER_ADDRESS, MQTT_PUBLISHER_ID);
            }

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(15);

            if (!instance.isConnected()) {
                instance.connect(options);
            }

        } catch (MqttException e) {
            e.printStackTrace();
        }

        return instance;
    }
}
