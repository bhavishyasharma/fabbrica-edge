package in.co.futech.fabbricaedge;

import java.util.List;

public class Measurement {
    private String name;
    private int frequency;
    private MeasurementType type;
    private List<MeasurementField> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public MeasurementType getType() {
        return type;
    }

    public void setType(MeasurementType type) {
        this.type = type;
    }

    public List<MeasurementField> getFields() {
        return fields;
    }

    public void setFields(List<MeasurementField> fields) {
        this.fields = fields;
    }

    public enum MeasurementType {
        RANDOM
    }
}
