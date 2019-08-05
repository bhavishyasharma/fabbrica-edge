package in.co.futech.fabbricaedge;

public class InvalidFieldException extends Exception {
    private String fieldName;
    private String measurementName;

    public InvalidFieldException(String fieldName, String measurementName) {
        this.fieldName = fieldName;
        this.measurementName = measurementName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMeasurementName() {
        return measurementName;
    }

    public void setMeasurementName(String measurementName) {
        this.measurementName = measurementName;
    }

    @Override
    public String getMessage() {
        return "Invalid Field " + fieldName + " in Measurement " + measurementName  + "!";
    }
}
