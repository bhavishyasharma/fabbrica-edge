package in.co.futech.fabbricaedge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.support.json.JsonObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfiguration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.util.Date;
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

    private ObjectMapper mapper;
    private Measurement measurement;
    private MeasurementSource measurementSource;

    public ScheduledTask(Measurement measurement) {
        this.measurement = measurement;
        switch (measurement.getType()) {
            case RANDOM: measurementSource = new RandomMeasurementSource();
            break;
            default: break;
        }
        mapper = new ObjectMapper();
    }

    public void runTask() {
        ObjectNode root = mapper.createObjectNode();
        try {
            measurementSource.populateValues(root, measurement);
        }
        catch (InvalidFieldException e) {
            FabbricaEdgeApplication.logger.error(e.getMessage());
        }
        root.put("name", measurement.getName());
        root.put("timestamp", System.currentTimeMillis());
        try {
            mqttGateway.send(mapper.writeValueAsString(root));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
