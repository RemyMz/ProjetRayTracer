package fr.imt.raytracer;

import fr.imt.raytracer.imaging.Color;
import fr.imt.raytracer.parsing.SceneFileParser;
import fr.imt.raytracer.renderer.RayTracer;
import fr.imt.raytracer.scene.Scene;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;

/**
 * Point d'entrée principal de l'application RayTracer.
 * <p>
 * Ce programme charge une description de scène, initialise le moteur de rendu,
 * active les optimisations (BVH), exécute le lancer de rayons en parallèle, et sauvegarde le résultat.
 */
public class Main {

    private static final String OUTPUT_DIRECTORY = "output";

    /**
     * Exécute le pipeline de rendu complet.
     *
     * @param args Arguments ligne de commande. Attend le chemin du fichier .scene en unique argument.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            printUsage();
            return;
        }

        String sceneFilePath = args[0];
        System.out.println(">>> Démarrage du RayTracer IMT");
        System.out.println(">>> Chargement de la scène : " + sceneFilePath);

        try {
            // 1. Parsing, Configuration ET Optimisation
            Scene scene = loadAndParseScene(sceneFilePath);
            printSceneSummary(scene);

            // 2. Rendu (Calcul intensif)
            System.out.println("\n>>> Lancement du rendu (Mode Parallèle)...");
            long startTime = System.currentTimeMillis();

            BufferedImage renderedImage = renderScene(scene);

            long duration = System.currentTimeMillis() - startTime;
            System.out.println(String.format(">>> Rendu terminé en %.3f secondes.", duration / 1000.0));

            // 3. Sauvegarde
            saveImage(renderedImage, scene.getOutputFilename());

        } catch (Exception e) {
            System.err.println("\n!!! ECHEC DU RENDU !!!");
            System.err.println("Erreur critique : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Charge, parse et optimise le fichier de description de scène.
     */
    private static Scene loadAndParseScene(String filePath) throws IOException {
        Scene scene = new Scene();
        SceneFileParser parser = new SceneFileParser(scene);
        parser.parseFile(filePath);
        
        // --- ACTIVATION DE L'OPTIMISATION BVH ---
        // C'est ici que la magie opère : on organise les triangles dans l'arbre.
        scene.buildBVH();
        // ----------------------------------------
        
        return scene;
    }

    /**
     * Exécute le moteur de rendu en utilisant le multi-threading.
     * <p>
     * Utilise {@link IntStream#parallel()} pour distribuer le calcul des lignes de l'image
     * sur tous les cœurs processeurs disponibles.
     *
     * @param scene La scène configurée.
     * @return L'image bitmap résultante.
     */
    private static BufferedImage renderScene(Scene scene) {
        RayTracer rayTracer = new RayTracer(scene);
        int width = scene.getWidth();
        int height = scene.getHeight();

        // TYPE_INT_RGB est le format le plus performant pour l'écriture directe des pixels
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Parallélisation sur l'axe vertical (les lignes)
        // BufferedImage.setRGB est thread-safe tant que les threads écrivent sur des coordonnées disjointes.
        IntStream.range(0, height).parallel().forEach(y -> {
            for (int x = 0; x < width; x++) {
                // Calcul de la couleur (Le coeur du RayTracing)
                Color pixelColor = rayTracer.getPixelColor(x, y);
                
                // Écriture directe dans le buffer image
                image.setRGB(x, y, pixelColor.toRGB());
            }
        });

        return image;
    }

    /**
     * Sauvegarde l'image générée sur le disque.
     */
    private static void saveImage(BufferedImage image, String filename) throws IOException {
        File outputDir = new File(OUTPUT_DIRECTORY);
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new IOException("Impossible de créer le répertoire de sortie : " + OUTPUT_DIRECTORY);
        }

        File outputFile = new File(outputDir, filename);
        if (!ImageIO.write(image, "png", outputFile)) {
            throw new IOException("Aucun écrivain PNG trouvé pour sauvegarder l'image.");
        }

        System.out.println(">>> Image sauvegardée avec succès : " + outputFile.getAbsolutePath());
    }

    private static void printSceneSummary(Scene scene) {
        System.out.println("--------------------------------------------------");
        System.out.println(" Résumé de la configuration");
        System.out.println("--------------------------------------------------");
        System.out.println(String.format(" Résolution      : %d x %d pixels", scene.getWidth(), scene.getHeight()));
        System.out.println(String.format(" Fichier cible   : %s/%s", OUTPUT_DIRECTORY, scene.getOutputFilename()));
        
        if (scene.getCamera() != null) {
            System.out.println(" Caméra          : " + scene.getCamera().getLookFrom());
        }
        
        System.out.println(" Lumières        : " + scene.getLights().size());
        // Note : Après buildBVH(), getShapes() ne retournera que les Plans + 1 BvhNode racine.
        // Ce n'est pas un bug d'affichage, c'est la preuve que la compression a fonctionné.
        System.out.println(" Objets Racine   : " + scene.getShapes().size() + " (Optimisé)");
        System.out.println(" Profondeur Max  : " + scene.getMaxDepth() + " rebonds");
        System.out.println("--------------------------------------------------");
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar raytracer.jar <fichier_scene.scene>");
    }
}