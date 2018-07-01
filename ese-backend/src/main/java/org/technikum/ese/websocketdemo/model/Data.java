package org.technikum.ese.websocketdemo.model;

public class Data {
    private String name;
    private String unit;
    private String payload;

    public String getName() {
        return name;
    }

    public Data(String name, String unit, String payload) {
        this.name = name;
        this.unit = unit;
        this.payload = payload;
    }

        public Data(){}
    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
