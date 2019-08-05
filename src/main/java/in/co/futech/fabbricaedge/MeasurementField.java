package in.co.futech.fabbricaedge;

public class MeasurementField {
    private String name;
    private FieldType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public enum  FieldType {
        Integer,
        Float,
        Boolean,
        String,
        Timestamp
    }
}
