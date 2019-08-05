package in.co.futech.fabbricaedge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

public interface MeasurementSource {
    public void populateValues(ObjectNode root, Measurement measurement) throws InvalidFieldException;
}
