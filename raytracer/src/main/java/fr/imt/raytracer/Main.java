package fr.imt.raytracer;

import fr.imt.raytracer.parsing.SceneFileParser;
import fr.imt.raytracer.scene.Scene;
import java.io.IOException;

/**
 * Point d'entrée principal pour le projet Raytracer.
 */
public class Main {

    public static void main(String[] args) {
        // 1. Vérifier qu'on a bien un argument (le fichier .scene)
        if (args.length != 1) {
            System.out.println("Erreur: Vous devez fournir un fichier .scene en paramètre.");
            System.out.println("Usage: java -jar raytracer.jar <scene.scene>");
            return;
        }

        String sceneFilePath = args[0];
        System.out.println("Chargement de la scène : " + sceneFilePath);

        try {
            // 2. Créer le parser et lire le fichier
            SceneFileParser parser = new SceneFileParser();
            Scene scene = parser.parse(sceneFilePath);

            // 3. Afficher un résumé pour valider le Jalon 2
            System.out.println("--- Validation Jalon 2 (Parsing) ---");
            System.out.println("Scène chargée avec succès !");
            System.out.println("  Taille image : " + scene.getWidth() + "x" + scene.getHeight());
            System.out.println("  Fichier sortie : " + scene.getOutputFilename());
            System.out.println("  Caméra (Position) : " + scene.getCamera().getLookFrom());
            System.out.println("  Lumière ambiante : " + scene.getAmbientLight());
            System.out.println("  Nombre de lumières : " + scene.getLights().size());
            System.out.println("  Nombre de formes : " + scene.getShapes().size());
            System.out.println("  Nombre de vertices : " + scene.getVertices().size());
            System.out.println("-------------------------------------");

            // TODO: (Pour le Jalon 3) Appeler le RayTracer pour générer l'image

        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier scène : " + e.getMessage());
        }
    }
}