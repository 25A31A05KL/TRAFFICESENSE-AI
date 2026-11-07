from fastapi import FastAPI, UploadFile, File, HTTPException
import cv2
import numpy as np

app = FastAPI(title="TrafficSense AI Engine", version="0.1.0")

@app.get("/")
def health():
    return {"service": "TrafficSense AI Engine", "status": "running",
            "note": "Inference models are extension points; no trained model is bundled."}

@app.post("/ai/detect-objects")
async def detect_objects(file: UploadFile = File(...)):
    raw = await file.read()
    image = cv2.imdecode(np.frombuffer(raw, np.uint8), cv2.IMREAD_COLOR)
    if image is None:
        raise HTTPException(400, "Uploaded file is not a readable image")
    # Placeholder: add a trained object-detection model here.
    return {"imageWidth": int(image.shape[1]), "imageHeight": int(image.shape[0]),
            "detections": [], "modelConfigured": False,
            "message": "Image received. Configure a trained detector to return object predictions."}

@app.post("/ai/detect-lanes")
async def detect_lanes(file: UploadFile = File(...)):
    raw = await file.read()
    image = cv2.imdecode(np.frombuffer(raw, np.uint8), cv2.IMREAD_COLOR)
    if image is None:
        raise HTTPException(400, "Uploaded file is not a readable image")
    # Basic image preprocessing only; this is not a production lane detector.
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    edges = cv2.Canny(gray, 60, 150)
    return {"imageWidth": int(image.shape[1]), "imageHeight": int(image.shape[0]),
            "edgePixels": int(np.count_nonzero(edges)),
            "laneModelConfigured": False,
            "message": "Preprocessing completed. Add calibrated lane geometry/model for lane output."}

@app.post("/ai/predict-traffic")
def predict_traffic(vehicle_count: int):
    if vehicle_count < 0:
        raise HTTPException(400, "vehicle_count must be non-negative")
    level = "LOW" if vehicle_count < 8 else "MEDIUM" if vehicle_count < 16 else "HIGH"
    return {"inputVehicleCount": vehicle_count, "baselineCategory": level,
            "modelConfigured": False,
            "message": "Rule-based baseline only; train a forecasting model for real prediction."}
