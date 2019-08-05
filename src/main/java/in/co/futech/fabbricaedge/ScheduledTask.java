package in.co.futech.fabbricaedge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class ScheduledTask {

    @Autowired
    private MqttGateway mqttGateway;

    @Autowired
    private EdgeConfiguration edgeConfiguration;

    private Random random = new Random();

    @Scheduled(fixedRate = 1000)
    public void run() {
        mqttGateway.send("fabbrica,topic="+ edgeConfiguration.getClientid() +" oilTemperature="+(random.nextInt(10)+30));
    }
}
