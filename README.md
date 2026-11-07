# TrafficSense AI – Autonomous Smart City Simulator

A student-friendly starter project combining Java Spring Boot, a browser-based traffic simulator,
MySQL, WebSocket-ready architecture, and an optional Python/OpenCV AI service.

## Important scope note
This repository is a **software simulation**, not a road-certified autonomous-driving system.
The browser simulation and Java APIs provide a working MVP. Computer-vision, prediction and
assistant integrations are included as extension points and require models/API credentials
before they can provide real AI inference.

## Features in this starter
- Browser-based 2D city simulation with moving vehicles
- Add vehicles and ambulance scenario
- Traffic density calculation and adaptive signal recommendation
- Graph-based shortest route (Dijkstra)
- Simulated obstacle / conflict alerts
- Weather mode controls
- Parking occupancy demo
- Accident event reporting
- Vehicle fleet/status dashboard
- Python FastAPI AI service scaffold for object/lane detection
- MySQL schema and Docker Compose
- REST API and project documentation

## Requirements
- Java 21+
- Maven 3.9+
- Python 3.10+ (optional AI service)
- Docker Desktop (optional; for MySQL)

## Run the Java application
```bash
cd backend-java
mvn spring-boot:run
```
Open http://localhost:8080

The app uses an in-memory H2 database by default, so it can run without installing MySQL.
H2 console: http://localhost:8080/h2-console
JDBC URL: `jdbc:h2:mem:trafficsense`

## Run with MySQL
From the project root:
```bash
docker compose up -d mysql
```
Then run Java with the MySQL profile:
```bash
cd backend-java
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```
Database schema is in `database/schema.sql`.

## Run optional Python AI service
```bash
cd ai-engine
python -m venv .venv
# Windows: .venv\Scripts\activate
# macOS/Linux: source .venv/bin/activate
pip install -r requirements.txt
uvicorn app:app --reload --port 8001
```
Open http://localhost:8001/docs. Detection endpoints currently return a clear
"model not configured" response until a model is added.

## Main API endpoints
- `GET /api/vehicles`
- `POST /api/vehicles` – JSON: `{"type":"CAR","emergency":false}`
- `GET /api/traffic/live`
- `GET /api/signals`
- `POST /api/routes/optimize` – JSON: `{"from":"A","to":"F"}`
- `POST /api/emergency/request` – JSON: `{"vehicleId":1}`
- `POST /api/accidents` – JSON: `{"roadId":"A-B","description":"Demo incident"}`
- `GET /api/parking`
- `POST /api/parking/{slotId}/occupy`
- `POST /api/weather` – JSON: `{"condition":"RAIN"}`
- `GET /api/fleet`
- `GET /api/analytics`

## Project structure
```text
TrafficSense-AI/
├── backend-java/       # Java 21 + Spring Boot REST API
├── frontend/           # Browser simulation (served by Spring Boot)
├── ai-engine/          # Optional Python FastAPI/OpenCV integration
├── database/           # MySQL schema and sample data
├── docs/               # Architecture, roadmap, testing notes
├── docker-compose.yml
└── README.md
```

## Safety
This project is intended for education and virtual simulation only. Do not connect it to a
real vehicle, traffic signal, or public-road infrastructure. Any real-world deployment would
require professional engineering, safety cases, regulatory approval and extensive validation.
