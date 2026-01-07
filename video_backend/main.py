import cv2
import pandas as pd
import numpy as np
import os
import uuid
import logging
from fastapi import FastAPI, BackgroundTasks, HTTPException
from pydantic import BaseModel
from typing import Dict, Optional, Tuple
from fastapi.middleware.cors import CORSMiddleware

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = FastAPI(title="Video Transcoder & Overlay API")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

tasks_status = {}

# --- NEW CONFIGURATION MODEL ---
class ColumnConfig(BaseModel):
    pos: Optional[Tuple[int, int]] = None
    font_color_hex: str = "#00FF00"  # Default Green
    bg_color_hex: Optional[str] = None  # Default No Background
    font_scale: float = 1.0  # Default Size
    thickness: int = 2

class ProcessRequest(BaseModel):
    video_input: str
    data_input: str
    video_output: str = "resultat_montage.mp4"
    config_colonnes: Dict[str, ColumnConfig] = {}
    decalage_temps: float = 0.0
    freq_refresh: float = 0.1

# --- UTILS FOR COLOR & DRAWING ---

def hex_to_bgr(hex_color: str) -> Tuple[int, int, int]:
    """Converts HEX string (#RRGGBB) to OpenCV BGR tuple."""
    hex_color = hex_color.lstrip('#')
    if len(hex_color) != 6:
        return (0, 255, 0) # Default to green on error
    r = int(hex_color[0:2], 16)
    g = int(hex_color[2:4], 16)
    b = int(hex_color[4:6], 16)
    return (b, g, r)

def draw_text_with_background(img, text, pos, font_scale, font_color_hex, thickness, bg_color_hex=None):
    """Draws text with an optional background box."""
    font = cv2.FONT_HERSHEY_SIMPLEX
    text_color = hex_to_bgr(font_color_hex)
    
    # Calculate text size to determine background box dimensions
    (text_w, text_h), baseline = cv2.getTextSize(text, font, font_scale, thickness)
    x, y = pos

    # Draw Background if requested
    if bg_color_hex:
        bg_color = hex_to_bgr(bg_color_hex)
        # Coordinates: x, y-height (top-left) to x+width, y+baseline (bottom-right)
        # Added a small padding (+5)
        cv2.rectangle(img, (x - 5, y - text_h - 5), (x + text_w + 5, y + 5), bg_color, -1)

    # Draw Text
    cv2.putText(img, text, (x, y), font, font_scale, text_color, thickness)

# --- LOGIQUE DE CONVERSION / MONTAGE ---

def run_video_engine(task_id: str, input_path: str, output_path: str, data_params: ProcessRequest = None):
    try:
        cap = cv2.VideoCapture(input_path)
        if not cap.isOpened():
            tasks_status[task_id] = {"status": "error", "message": "Impossible d'ouvrir la source"}
            return

        fps = cap.get(cv2.CAP_PROP_FPS)
        width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
        height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
        total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
        out = cv2.VideoWriter(output_path, cv2.VideoWriter_fourcc(*'mp4v'), fps, (width, height))

        df = None
        if data_params:
            df_raw = lire_fichier_donnees(data_params.data_input)
            if df_raw is not None:
                col_temps = df_raw.columns[0]
                df = pd.DataFrame({'time_sec': pd.to_numeric(df_raw[col_temps], errors='coerce')})
                for col in df_raw.columns[1:]:
                    df[col] = df_raw[col]
                df = df.dropna(subset=['time_sec']).sort_values('time_sec')

        frame_idx = 0
        while cap.isOpened():
            ret, frame = cap.read()
            if not ret: break

            if data_params and df is not None:
                video_time = frame_idx / fps
                data_time = ( (video_time - data_params.decalage_temps) // data_params.freq_refresh ) * data_params.freq_refresh
                
                for i, col_name in enumerate(df.columns[1:]):
                    # Get config or use defaults
                    config = data_params.config_colonnes.get(col_name)
                    
                    # Defaults
                    pos = (100, 150 + (i * 80))
                    font_color = "#00FF00"
                    bg_color = None
                    font_scale = 1.0
                    thickness = 2

                    # Override with config if exists
                    if config:
                        if config.pos: pos = config.pos
                        if config.font_color_hex: font_color = config.font_color_hex
                        if config.bg_color_hex: bg_color = config.bg_color_hex
                        if config.font_scale: font_scale = config.font_scale
                        if config.thickness: thickness = config.thickness

                    val = "..." if (video_time - data_params.decalage_temps) < 0 else trouver_valeur_proche(df, data_time, col_name)
                    
                    text_display = f"{col_name}: {val}"
                    
                    # Call the new drawing function
                    draw_text_with_background(
                        frame, 
                        text_display, 
                        pos, 
                        font_scale, 
                        font_color, 
                        thickness, 
                        bg_color
                    )

            out.write(frame)
            frame_idx += 1
            if frame_idx % 100 == 0:
                tasks_status[task_id] = {"status": "processing", "progress": f"{round((frame_idx/total_frames)*100, 1)}%"}

        cap.release()
        out.release()
        tasks_status[task_id] = {"status": "completed", "progress": "100%", "output": output_path}
    except Exception as e:
        tasks_status[task_id] = {"status": "error", "message": str(e)}

# --- ENDPOINTS (Unchanged structure) ---

@app.post("/video/raw")
async def convert_to_mp4(video_name: str, background_tasks: BackgroundTasks):
    if not os.path.exists(video_name):
        raise HTTPException(status_code=404, detail="Fichier source introuvable")
    
    task_id = str(uuid.uuid4())
    output_name = video_name.rsplit('.', 1)[0] + ".mp4"
    tasks_status[task_id] = {"status": "starting_conversion", "progress": "0%"}
    
    background_tasks.add_task(run_video_engine, task_id, video_name, output_name, None)
    return {"task_id": task_id, "message": f"Conversion de {video_name} lancée vers {output_name}"}

@app.post("/video/process")
async def start_montage(params: ProcessRequest, background_tasks: BackgroundTasks):
    logger.info(f"=== VIDEO PROCESS REQUEST ===")
    logger.info(f"video_input: {params.video_input}")
    logger.info(f"data_input: {params.data_input}")
    logger.info(f"video_output: {params.video_output}")
    
    # Debug: Check if parent directory exists
    parent_dir = os.path.dirname(params.video_input)
    if os.path.exists(parent_dir):
        logger.info(f"Parent dir exists: {parent_dir}")
        logger.info(f"Contents: {os.listdir(parent_dir)}")
    else:
        logger.info(f"Parent dir MISSING: {parent_dir}")
        # Try to list /shared/data
        if os.path.exists("/shared/data"):
            logger.info(f"/shared/data contents: {os.listdir('/shared/data')}")
    
    if not os.path.exists(params.video_input):
        logger.error(f"Video file not found: {params.video_input}")
        raise HTTPException(status_code=404, detail=f"Vidéo source introuvable: {params.video_input}")
    
    task_id = str(uuid.uuid4())
    tasks_status[task_id] = {"status": "starting_montage", "progress": "0%"}
    
    background_tasks.add_task(run_video_engine, task_id, params.video_input, params.video_output, params)
    return {"task_id": task_id, "message": "Montage lancé sur le serveur"}

@app.get("/video/status/{task_id}")
async def get_status(task_id: str):
    status = tasks_status.get(task_id)
    if not status: raise HTTPException(status_code=404, detail="Task ID inconnu")
    return status

# --- UTILS (Data Reading - Unchanged) ---
def trouver_valeur_proche(df, temps_cible, nom_colonne):
    idx = np.searchsorted(df['time_sec'], temps_cible, side="left")
    if idx >= len(df): return df.iloc[-1][nom_colonne]
    if idx == 0: return df.iloc[0][nom_colonne]
    col_idx = idx - 1 if abs(temps_cible - df.iloc[idx-1]['time_sec']) < abs(temps_cible - df.iloc[idx]['time_sec']) else idx
    return df.iloc[col_idx][nom_colonne]

def lire_fichier_donnees(chemin):
    try:
        if chemin.endswith('.xlsx') or chemin.endswith('.xls'): return pd.read_excel(chemin)
        return pd.read_csv(chemin, sep=None, engine='python', encoding='utf-8')
    except: return None

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)