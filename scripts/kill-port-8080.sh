#!/bin/bash
PORT=8080
PID=$(lsof -ti tcp:"$PORT")

if [ -n "$PID" ]; then
  echo "Port $PORT already in use by PID $PID - killing process..."
  kill -9 "$PID"
else
  echo "Port $PORT is free."
fi