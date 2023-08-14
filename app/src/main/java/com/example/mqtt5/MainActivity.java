package com.example.mqtt5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    private String ServerIP = "";
    private String TOPIC = "SF1";

    private MqttClient mqttClient = null;
    private TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageTextView = findViewById(R.id.message_text_view);

        Button button = findViewById(R.id.button_send);

        try {
            mqttClient = new MqttClient(ServerIP, MqttClient.generateClientId(), null);
            mqttClient.connect();

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        mqttClient.publish(TOPIC, new MqttMessage("hello!".getBytes()));
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            });

            mqttClient.subscribe(TOPIC);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    Log.d("MQTTService", "Connection Lost");
                }

                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    String message = "Message Arrived : " + mqttMessage.toString();
                    Log.d("MQTTService", message);

                    // 화면에 메시지 출력
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messageTextView.setText(message);
                        }
                    });
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    Log.d("MQTTService", "Delivery Complete");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
