package fr.imt.raytracer.parsing;

import fr.imt.raytracer.geometry.Point;
import fr.imt.raytracer.geometry.Sphere;
import fr.imt.raytracer.geometry.Vector;
import fr.imt.raytracer.imaging.Color;
import fr.imt.raytracer.scene.Camera;
import fr.imt.raytracer.scene.DirectionalLight;
import fr.imt.raytracer.scene.PointLight;
import fr.imt.raytracer.scene.Scene;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Lit un fichier de description de scène (.scene) et construit un objet Scene.
 * (Jalon 2)
 */
public class SceneFileParser {

    private final Scene scene;

    // Variables pour mémoriser l'état des matériaux (Jalon 2 - Aide 4)
    // Celles-ci sont lues et stockées avant d'être appliquées à une forme.
    private Color currentDiffuse = new Color(0, 0, 0);
    private Color currentSpecular = new Color(0, 0, 0);
    private double currentShininess = 0.0; // (Jalon 5)

    // Liste des points/vertex (pour les triangles)
    // On la stocke ici temporairement pour les retrouver par leur index
    private java.util.List<Point> vertices = new java.util.ArrayList<>();


    /**
     * Constructeur. Initialise une nouvelle scène vide.
     */
    public SceneFileParser() {
        this.scene = new Scene();
    }

    /**
     * Point d'entrée principal. Lit et parse le fichier.
     *
     * @param filename Le chemin vers le fichier .scene.
     * @return L'objet Scene complet.
     * @throws IOException Si le fichier ne peut être lu.
     */
    public Scene parse(String filename) throws IOException {
        // Utilise un try-with-resources pour s'assurer que le lecteur est fermé
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            // Lit le fichier ligne par ligne
            while ((line = reader.readLine()) != null) {
                parseLine(line);
            }
        }
        // Ajoute les vertices collectés à la scène
        for (Point v : vertices) {
            scene.addVertex(v);
        }
        return scene;
    }

    /**
     * Analyse une seule ligne du fichier de scène.
     *
     * @param line La ligne à analyser.
     */
    private void parseLine(String line) {
        // Nettoyer la ligne (enlever les espaces avant/après)
        line = line.trim();

        // Ignorer les lignes vides ou les commentaires (Jalon 2)
        if (line.isEmpty() || line.startsWith("#")) {
            return;
        }

        // Séparer la commande (ex: "size") de ses arguments
        // \\s+ signifie "un ou plusieurs espaces/tabulations"
        String[] parts = line.split("\\s+");
        String command = parts[0];

        // Gérer chaque commande (Jalon 2 - Aide 2)
        try {
            switch (command) {
                case "size": // size width height
                    scene.setWidth(Integer.parseInt(parts[1]));
                    scene.setHeight(Integer.parseInt(parts[2]));
                    break;
                case "output": // output filename.png
                    scene.setOutputFilename(parts[1]);
                    break;
                case "camera": // camera x y z lookAtX lookAtY lookAtZ upX upY upZ fov
                    Point lookFrom = new Point(d(parts[1]), d(parts[2]), d(parts[3]));
                    Point lookAt = new Point(d(parts[4]), d(parts[5]), d(parts[6]));
                    Vector up = new Vector(d(parts[7]), d(parts[8]), d(parts[9]));
                    double fov = d(parts[10]);
                    scene.setCamera(new Camera(lookFrom, lookAt, up, fov));
                    break;
                
                // --- Matériaux et Lumières ---
                case "ambient": // ambient r g b
                    scene.setAmbientLight(new Color(d(parts[1]), d(parts[2]), d(parts[3])));
                    break;
                case "diffuse": // diffuse r g b
                    this.currentDiffuse = new Color(d(parts[1]), d(parts[2]), d(parts[3]));
                    break;
                case "specular": // specular r g b
                    this.currentSpecular = new Color(d(parts[1]), d(parts[2]), d(parts[3]));
                    break;
                case "shininess": // shininess s (Jalon 5)
                    this.currentShininess = d(parts[1]);
                    break;
                case "directional": // directional x y z r g b
                    Vector dir = new Vector(d(parts[1]), d(parts[2]), d(parts[3]));
                    Color colorDir = new Color(d(parts[4]), d(parts[5]), d(parts[6]));
                    scene.addLight(new DirectionalLight(dir, colorDir));
                    break;
                case "point": // point x y z r g b
                    // *** C'EST LA LIGNE CORRIGÉE ***
                    Point pos = new Point(d(parts[1]), d(parts[2]), d(parts[3]));
                    Color colorPt = new Color(d(parts[4]), d(parts[5]), d(parts[6]));
                    scene.addLight(new PointLight(pos, colorPt));
                    break;

                // --- Formes Géométriques ---
                case "sphere": // sphere x y z r
                    Point center = new Point(d(parts[1]), d(parts[2]), d(parts[3]));
                    double radius = d(parts[4]);
                    // Crée la sphère en utilisant les derniers matériaux lus
                    Sphere sphere = new Sphere(center, radius, currentDiffuse, currentSpecular, currentShininess);
                    scene.addShape(sphere);
                    break;
                
                // --- Triangles (Jalon 6 - Bonus) ---
                case "maxverts":
                    // Initialise la liste des vertices avec la bonne taille
                    this.vertices = new java.util.ArrayList<>(Integer.parseInt(parts[1]));
                    break;
                case "vertex": // vertex x y z
                    Point vert = new Point(d(parts[1]), d(parts[2]), d(parts[3]));
                    this.vertices.add(vert);
                    break;
                case "tri": // tri v1 v2 v3
                    // Pas encore implémenté (Jalon 3 se concentre sur les sphères)
                    System.out.println("Parsing de 'tri' non implémenté (focus Jalon 3: sphères).");
                    break;
                
                // --- Plans (Jalon 6 - Bonus) ---
                case "plane": // plane x y z nx ny nz
                    // Pas encore implémenté
                    System.out.println("Parsing de 'plane' non implémenté (focus Jalon 3: sphères).");
                    break;

                default:
                    System.err.println("Commande inconnue dans le fichier scène : " + command);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'analyse de la ligne : '" + line + "' - Erreur: " + e.getMessage());
            e.printStackTrace(); // Utile pour le débogage
        }
    }

    /**
     * Raccourci pour parser un Double.
     * @param s La chaîne à parser.
     * @return Le double.
     */
    private double d(String s) {
        return Double.parseDouble(s);
    }
}