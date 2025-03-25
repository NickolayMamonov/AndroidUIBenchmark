@echo off

echo =============================================
echo Building app-bdui without Hilt
echo =============================================

echo Step 1: Stop Gradle daemon
call gradlew --stop
timeout /t 2 > NUL

echo Step 2: Building only app-bdui module
call gradlew :app-bdui:clean :app-bdui:assembleDebug --no-build-cache --info

echo =============================================
echo Build process completed
echo =============================================
