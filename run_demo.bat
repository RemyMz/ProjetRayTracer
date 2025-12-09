@echo off
echo >>> 1. Nettoyage et Compilation...
call mvn clean package -DskipTests

echo >>> 2. Lancement du Rendu Final (Dragon)...
java -jar raytracer/target/raytracer-1.0-SNAPSHOT.jar scenes/final_avec_bonus.scene

echo >>> 3. Termine ! L'image est dans output/
pause