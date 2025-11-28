package model;

import java.time.Instant;

public class HeaderDataType {
    private String className;
    private Instant timestamp;

    public HeaderDataType() {}
    public HeaderDataType(String className, Instant timestamp) {
        this.className = className;
        this.timestamp = timestamp;
    }

    public String getClassName() {return className;}
    public void setClassName(String className) {this.className = className;}

    public Instant getTimestamp() {return timestamp;}
    public void setTimestamp(Instant timestamp) {this.timestamp = timestamp;}
}
