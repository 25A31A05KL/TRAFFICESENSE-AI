CREATE DATABASE IF NOT EXISTS trafficsense;
USE trafficsense;

CREATE TABLE IF NOT EXISTS vehicles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  vehicle_type VARCHAR(30) NOT NULL,
  is_emergency BOOLEAN NOT NULL DEFAULT FALSE,
  status VARCHAR(30) NOT NULL DEFAULT 'IDLE',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS roads (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  road_code VARCHAR(30) NOT NULL UNIQUE,
  source_node VARCHAR(20) NOT NULL,
  destination_node VARCHAR(20) NOT NULL,
  distance DOUBLE NOT NULL
);
CREATE TABLE IF NOT EXISTS traffic_signals (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  signal_code VARCHAR(30) NOT NULL UNIQUE,
  road_code VARCHAR(30) NOT NULL,
  signal_status VARCHAR(15) NOT NULL,
  green_duration_seconds INT NOT NULL DEFAULT 30
);
CREATE TABLE IF NOT EXISTS traffic_records (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  road_code VARCHAR(30) NOT NULL,
  vehicle_count INT NOT NULL,
  density VARCHAR(15) NOT NULL,
  recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS parking_slots (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  slot_code VARCHAR(30) NOT NULL UNIQUE,
  location_name VARCHAR(100) NOT NULL,
  is_available BOOLEAN NOT NULL DEFAULT TRUE
);
CREATE TABLE IF NOT EXISTS incidents (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  road_code VARCHAR(30),
  description VARCHAR(500),
  status VARCHAR(30) NOT NULL DEFAULT 'REPORTED',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE IF NOT EXISTS weather_records (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  condition_name VARCHAR(30) NOT NULL,
  visibility_km DOUBLE,
  recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT IGNORE INTO roads(road_code,source_node,destination_node,distance) VALUES
('A-B','A','B',4),('A-D','A','D',2),('B-C','B','C',3),
('B-E','B','E',5),('D-E','D','E',2),('E-F','E','F',3),('C-F','C','F',4);
INSERT IGNORE INTO parking_slots(slot_code,location_name,is_available) VALUES
('P-01','North Parking',TRUE),('P-02','North Parking',TRUE),
('P-03','Central Parking',FALSE),('P-04','South Parking',TRUE);
