# Architecture

## Logical flow
Browser Canvas simulation -> Java Spring Boot REST API -> domain services.
The optional Python FastAPI service handles image preprocessing and is the integration
boundary for future trained computer-vision models.

## Feature mapping
- Vehicle simulation: frontend Canvas + vehicle API
- Object/lane detection: Python AI service extension points
- Collision avoidance: simulated obstacle/conflict rules (extend with validated safety logic)
- Adaptive signals: traffic-density baseline
- Emergency corridor: simulated emergency priority workflow
- Route optimization: Dijkstra implementation over sample graph
- V2V: planned event messages over WebSocket
- Traffic prediction: baseline category; forecasting model is future work
- Smart parking: parking API and demo slots
- Accident detection: user-triggered simulated incident endpoint
- Weather: selectable virtual weather state
- Pedestrian safety: planned virtual crossing actor and rules
- Fleet: registered vehicle list
- Digital Twin: browser-based virtual city
- Assistant: planned natural-language interface over verified application data

## Extension boundary
Do not connect this prototype to physical vehicles, public traffic lights or road infrastructure.
