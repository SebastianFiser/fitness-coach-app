#!/bin/bash

set -e

echo "Compiling incremental build... "
./gradlew assembleDebug --ofline

echo "installing APK to device..."
./gradlew installDebug

if ! pgrep -x "scrcpy" > /dev/null; then
    echo "launching mirrior "
    scrcpy --max-fps=60 --window-title="FCA"
else
    echo "updated on screen !"
fi
