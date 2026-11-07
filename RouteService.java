package com.trafficsense.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class RouteService {
    private final Map<String, Map<String, Integer>> graph = new HashMap<>();
    public RouteService() {
        connect("A","B",4); connect("A","D",2); connect("B","C",3);
        connect("B","E",5); connect("D","E",2); connect("E","F",3);
        connect("C","F",4); connect("D","B",2);
    }
    private void connect(String a, String b, int w) {
        graph.computeIfAbsent(a,k->new HashMap<>()).put(b,w);
        graph.computeIfAbsent(b,k->new HashMap<>()).put(a,w);
    }
    public Map<String,Object> shortest(String start, String end) {
        Map<String,Integer> dist = new HashMap<>();
        Map<String,String> prev = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(n -> dist.getOrDefault(n, Integer.MAX_VALUE)));
        graph.keySet().forEach(n -> dist.put(n, Integer.MAX_VALUE));
        if (!dist.containsKey(start) || !dist.containsKey(end)) throw new IllegalArgumentException("Use nodes A to F");
        dist.put(start,0); pq.add(start);
        while (!pq.isEmpty()) {
            String u = pq.poll();
            if (u.equals(end)) break;
            for (var edge : graph.getOrDefault(u,Map.of()).entrySet()) {
                int nd = dist.get(u) + edge.getValue();
                if (nd < dist.get(edge.getKey())) {
                    dist.put(edge.getKey(),nd); prev.put(edge.getKey(),u); pq.remove(edge.getKey()); pq.add(edge.getKey());
                }
            }
        }
        List<String> path = new ArrayList<>();
        for (String at=end; at!=null; at=prev.get(at)) path.add(at);
        Collections.reverse(path);
        Map<String,Object> result = new LinkedHashMap<>();
        result.put("path",path); result.put("distance",dist.get(end));
        result.put("algorithm","Dijkstra"); return result;
    }
}
