package in.co.futech.fabbricaedge;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(defaultRequestChannel = "mqttChannel")
public interface MqttGateway {
    void send(String data);
}
