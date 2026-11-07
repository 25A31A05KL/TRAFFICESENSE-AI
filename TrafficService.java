package com.trafficsense.service;

import com.trafficsense.model.Vehicle;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TrafficService {
    private final List<Vehicle> vehicles = new CopyOnWriteArrayList<>();
    private final AtomicLong ids = new AtomicLong(0);
    private String weather = "CLEAR";
    private final Map<String, Boolean> parking = new LinkedHashMap<>();

    public TrafficService() {
        for (int i = 1; i <= 8; i++) vehicles.add(new Vehicle(ids.incrementAndGet(), "CAR", false));
        parking.put("P-01", true); parking.put("P-02", true);
        parking.put("P-03", false); parking.put("P-04", true);
    }
    public List<Vehicle> vehicles() { return vehicles; }
    public Vehicle add(String type, boolean emergency) {
        Vehicle v = new Vehicle(ids.incrementAndGet(), type.toUpperCase(Locale.ROOT), emergency);
        vehicles.add(v); return v;
    }
    public Map<String,Object> live() {
        long count = vehicles.size();
        String density = count < 8 ? "LOW" : count < 16 ? "MEDIUM" : "HIGH";
        int greenSeconds = density.equals("HIGH") ? 45 : density.equals("MEDIUM") ? 30 : 20;
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("vehicleCount", count); m.put("density", density);
        m.put("recommendedGreenSeconds", greenSeconds);
        m.put("weather", weather);
        m.put("emergencyVehicles", vehicles.stream().filter(Vehicle::isEmergency).count());
        return m;
    }
    public List<Map<String,Object>> signals() {
        Map<String,Object> a = new LinkedHashMap<>();
        a.put("id","SIG-A"); a.put("road","North-South"); a.put("status","GREEN");
        a.put("recommendedGreenSeconds", live().get("recommendedGreenSeconds"));
        Map<String,Object> b = new LinkedHashMap<>();
        b.put("id","SIG-B"); b.put("road","East-West"); b.put("status","RED");
        b.put("recommendedGreenSeconds", live().get("recommendedGreenSeconds"));
        return List.of(a,b);
    }
    public String weather() { return weather; }
    public void weather(String condition) { this.weather = condition.toUpperCase(Locale.ROOT); }
    public Map<String,Boolean> parking() { return parking; }
    public boolean occupy(String id) {
        if (!parking.containsKey(id)) throw new NoSuchElementException("Unknown parking slot");
        if (!parking.get(id)) return false;
        parking.put(id, false); return true;
    }
    public Map<String,Object> analytics() {
        Map<String,Object> a = new LinkedHashMap<>(live());
        a.put("availableParking", parking.values().stream().filter(Boolean::booleanValue).count());
        a.put("totalParking", parking.size());
        a.put("incidents", 0);
        return a;
    }
}
