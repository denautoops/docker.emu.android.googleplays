#!/bin/bash

emulator @Nexus_5X_API_28 &

while [ "`adb shell getprop sys.boot_completed | tr -d '\r' `" != "1" ] ; do sleep 1; done

appium --port $1 &

wait
