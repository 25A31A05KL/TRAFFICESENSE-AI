# Manual smoke tests

- [ ] `mvn spring-boot:run` starts on port 8080.
- [ ] Home page loads at http://localhost:8080.
- [ ] Start/Pause/Reset controls work.
- [ ] Add vehicle increases virtual vehicle count.
- [ ] Add ambulance creates emergency-colored vehicle.
- [ ] Weather selection changes simulation overlay.
- [ ] Obstacle toggle displays/removes obstacle.
- [ ] Route A to F returns a Dijkstra path.
- [ ] Parking slots load from backend.
- [ ] Report incident creates an accident record.
- [ ] Python `/` health endpoint responds on port 8001.
- [ ] Python image endpoints reject invalid image uploads.

Note: visual vehicle motion is client-side simulation; the starter does not yet provide a
validated autonomous-driving model or real sensor integration.
