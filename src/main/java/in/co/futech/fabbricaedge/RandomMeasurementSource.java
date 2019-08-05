package in.co.futech.fabbricaedge;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class RandomMeasurementSource implements MeasurementSource {

    private Random random = new Random();

    @Override
    public void populateValues(ObjectNode root, Measurement measurement) throws InvalidFieldException {
        for(MeasurementField measurementField: measurement.getFields()) {
            switch (measurementField.getType()) {
                case Integer: root.put(measurementField.getName(), random.nextInt(1000));
                break;
                case Float: root.put(measurementField.getName(),random.nextFloat());
                break;
                case Boolean: root.put(measurementField.getName(), random.nextBoolean());
                break;
                case String: root.put(measurementField.getName(), UUID.randomUUID().toString());
                break;
                case Timestamp: root.put(measurementField.getName(), System.currentTimeMillis());
                break;
                default:
                    throw new InvalidFieldException(measurementField.getName(), measurement.getName());
            }
        }
    }
}
