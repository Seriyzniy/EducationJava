package model;

import java.util.UUID;

public class CustomHeader {
    private String header;
    private UUID uuid;
    private int x;

    public CustomHeader() {}

    public CustomHeader(String header, UUID uuid, int x) {
        this.header = header;
        this.uuid = uuid;
        this.x = x;
    }

    public String getHeader() {return header;}
    public void setHeader(String header) {this.header = header;}

    public UUID getUuid() {return uuid;}
    public void setUuid(UUID uuid) {this.uuid = uuid;}

    public int getX() {return x;}
    public void setX(int x) {this.x = x;}

    @Override
    public String toString() {
        return "header:" + header + ", uuid:" + uuid + ", x:" + x;
    }
}
