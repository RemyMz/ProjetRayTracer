#!/bin/bash
echo ">>> 1. Nettoyage et Compilation..."
mvn clean package -DskipTests

echo ">>> 2. Lancement du Rendu Final (Dragon)..."
java -jar raytracer/target/raytracer-1.0-SNAPSHOT.jar scenes/final_avec_bonus.scene

echo ">>> 3. Ouverture de l'image..."
open output/dragon3.png || xdg-open output/dragon3.png