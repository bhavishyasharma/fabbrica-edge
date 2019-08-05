package in.co.futech.fabbricaedge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfiguration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Component
@Scope("prototype")
public class ScheduledTask {

    @Autowired
    private MqttGateway mqttGateway;

    @Autowired
    private EdgeConfiguration edgeConfiguration;

    private List<EdgeConfiguration.Measurement> measurements;

    private Random random = new Random();

    public ScheduledTask(List<EdgeConfiguration.Measurement> measurements) {
        this.measurements = measurements;
    }

    public void runTask() {
        mqttGateway.send("fabbrica,topic="+ edgeConfiguration.getClientid() + " " + measurements.get(0).getName() + "="+(random.nextInt(10)+30));
    }
}
