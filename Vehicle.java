package com.trafficsense.model;

public class Vehicle {
    private long id;
    private String type;
    private boolean emergency;
    private String status;
    private double x;
    private double y;
    private double speed;

    public Vehicle() {}
    public Vehicle(long id, String type, boolean emergency) {
        this.id = id; this.type = type; this.emergency = emergency;
        this.status = "MOVING"; this.speed = emergency ? 1.4 : 1.0;
    }
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public boolean isEmergency() { return emergency; }
    public void setEmergency(boolean emergency) { this.emergency = emergency; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }
}
