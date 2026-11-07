package com.trafficsense.controller;

import com.trafficsense.service.TrafficService;
import com.trafficsense.service.RouteService;
import com.trafficsense.model.Vehicle;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TrafficController {
    private final TrafficService traffic;
    private final RouteService routes;
    private final List<Map<String,Object>> accidents = new CopyOnWriteArrayList<>();
    public TrafficController(TrafficService traffic, RouteService routes) {
        this.traffic = traffic; this.routes = routes;
    }
    @GetMapping("/vehicles") public List<Vehicle> vehicles() { return traffic.vehicles(); }
    @PostMapping("/vehicles") public Vehicle addVehicle(@RequestBody Map<String,Object> body) {
        return traffic.add(String.valueOf(body.getOrDefault("type","CAR")),
            Boolean.parseBoolean(String.valueOf(body.getOrDefault("emergency",false))));
    }
    @GetMapping("/traffic/live") public Map<String,Object> live() { return traffic.live(); }
    @GetMapping("/signals") public List<Map<String,Object>> signals() { return traffic.signals(); }
    @PostMapping("/routes/optimize") public Map<String,Object> route(@RequestBody Map<String,String> body) {
        return routes.shortest(body.getOrDefault("from","A"), body.getOrDefault("to","F"));
    }
    @PostMapping("/emergency/request") public Map<String,Object> emergency(@RequestBody Map<String,Object> body) {
        long id = Long.parseLong(String.valueOf(body.getOrDefault("vehicleId","0")));
        return Map.of("accepted",true,"vehicleId",id,"message","Emergency priority enabled in simulation");
    }
    @GetMapping("/parking") public Map<String,Boolean> parking() { return traffic.parking(); }
    @PostMapping("/parking/{slotId}/occupy") public Map<String,Object> occupy(@PathVariable String slotId) {
        return Map.of("slotId",slotId,"occupied",traffic.occupy(slotId));
    }
    @PostMapping("/weather") public Map<String,Object> weather(@RequestBody Map<String,String> body) {
        traffic.weather(body.getOrDefault("condition","CLEAR"));
        return Map.of("condition",traffic.weather());
    }
    @PostMapping("/accidents") public Map<String,Object> accident(@RequestBody Map<String,String> body) {
        Map<String,Object> event = new LinkedHashMap<>();
        event.put("id",accidents.size()+1); event.put("roadId",body.getOrDefault("roadId","A-B"));
        event.put("description",body.getOrDefault("description","Demo incident"));
        event.put("status","REPORTED"); accidents.add(event); return event;
    }
    @GetMapping("/accidents") public List<Map<String,Object>> accidents() { return accidents; }
    @GetMapping("/fleet") public Map<String,Object> fleet() {
        return Map.of("vehicles",traffic.vehicles(),"total",traffic.vehicles().size());
    }
    @GetMapping("/analytics") public Map<String,Object> analytics() { return traffic.analytics(); }
}
