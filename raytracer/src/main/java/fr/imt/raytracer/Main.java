package fr.imt.raytracer;

import fr.imt.raytracer.parsing.SceneFileParser;
import fr.imt.raytracer.scene.Scene;
import java.io.IOException;

// --- IMPORTS AJOUTÉS (Jalon 3) ---
import fr.imt.raytracer.renderer.RayTracer;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
// ---

/**
 * Point d'entrée principal pour le projet Raytracer.
 * (Mis à jour Jalon 3)
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
            // 2. Créer le parser et lire le fichier (Jalon 2)
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

            // --- DÉBUT JALON 3 : Rendu de l'image ---
            
            System.out.println("\nLancement du rendu (Jalon 3)...");

            // 4. Créer le moteur de rendu
            RayTracer rayTracer = new RayTracer(scene);

            // 5. Lancer le rendu (cela peut prendre quelques secondes)
            BufferedImage image = rayTracer.render();

            System.out.println("Rendu terminé.");

            // 6. Sauvegarder l'image
            String filename = scene.getOutputFilename();
            
            // Créer le dossier "output" à la racine s'il n'existe pas
            File outputDir = new File("output");
            if (!outputDir.exists()) {
                outputDir.mkdir();
            }
            
            // Le chemin de sauvegarde sera "output/mascene.png"
            File outputFile = new File(outputDir, filename);
            
            // Écrire l'image au format PNG
            ImageIO.write(image, "png", outputFile);

            System.out.println("Image sauvegardée dans : " + outputFile.getPath());
            
            // --- FIN JALON 3 ---

        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier scène : " + e.getMessage());
        } catch (Exception e) {
            // Attraper d'autres erreurs potentielles (ex: rendu)
            System.err.println("Une erreur est survenue lors du rendu : " + e.getMessage());
            e.printStackTrace();
        }
    }
}