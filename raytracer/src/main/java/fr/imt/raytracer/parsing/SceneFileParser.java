package fr.imt.raytracer.parsing;

import fr.imt.raytracer.geometry.*;
import fr.imt.raytracer.imaging.Color;
import fr.imt.raytracer.scene.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Analyseur syntaxique pour les fichiers de description de scène (.scene).
 * <p>
 * Lit un fichier texte ligne par ligne, extrait les commandes et configure l'objet {@link Scene}.
 * Gère une machine à états pour les propriétés matérielles (couleurs, brillance) qui s'appliquent
 * aux objets déclarés ultérieurement.
 */
public class SceneFileParser {

    // --- Constantes par défaut ---
    private static final Color DEFAULT_DIFFUSE = new Color(0.8, 0.8, 0.8);
    private static final Color DEFAULT_SPECULAR = new Color(0.0, 0.0, 0.0);
    private static final double DEFAULT_SHININESS = 10.0;

    private final Scene scene;

    // --- État mutable du parseur ---
    private Color currentDiffuse = DEFAULT_DIFFUSE;
    private Color currentSpecular = DEFAULT_SPECULAR;
    private double currentShininess = DEFAULT_SHININESS;

    // Buffer pour les sommets (Vertex) des maillages
    private final List<Point> vertices = new ArrayList<>();
    private int maxVertices = 0;

    /**
     * Initialise le parseur pour une scène donnée.
     * @param scene L'objet scène à peupler.
     */
    public SceneFileParser(Scene scene) {
        this.scene = scene;
    }

    /**
     * Parse le fichier spécifié et remplit la scène.
     *
     * @param filename Chemin vers le fichier de scène.
     * @throws FileNotFoundException Si le fichier n'existe pas.
     * @throws IllegalArgumentException Si le fichier contient une erreur de syntaxe.
     */
    public void parseFile(String filename) throws FileNotFoundException {
        File file = new File(filename);

        try (Scanner scanner = new Scanner(file)) {
            int lineNumber = 0;
            
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                lineNumber++;

                // Ignorer lignes vides et commentaires
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                try {
                    processLine(line);
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                        String.format("Erreur de syntaxe ligne %d : %s", lineNumber, e.getMessage()), e
                    );
                }
            }
        }
    }

    /**
     * Traite une ligne de commande individuelle.
     */
    private void processLine(String line) {
        String[] tokens = line.split("\\s+");
        String command = tokens[0];

        switch (command) {
            // --- Configuration Globale ---
            case "size":
                scene.setWidth(Integer.parseInt(tokens[1]));
                scene.setHeight(Integer.parseInt(tokens[2]));
                break;
            case "output":
                scene.setOutputFilename(tokens[1]);
                break;
            case "maxdepth":
                scene.setMaxDepth(Integer.parseInt(tokens[1]));
                break;
            case "ambient":
                scene.setAmbient(parseColor(tokens, 1));
                break;

            // --- Caméra ---
            case "camera":
                handleCamera(tokens);
                break;

            // --- Sources de Lumière ---
            case "directional":
                handleDirectionalLight(tokens);
                break;
            case "point":
                handlePointLight(tokens);
                break;

            // --- Matériaux (État) ---
            case "diffuse":
                currentDiffuse = parseColor(tokens, 1);
                break;
            case "specular":
                currentSpecular = parseColor(tokens, 1);
                break;
            case "shininess":
                currentShininess = Double.parseDouble(tokens[1]);
                break;

            // --- Géométrie : Définition des sommets ---
            case "maxverts":
                maxVertices = Integer.parseInt(tokens[1]);
                // Optionnel : vertices = new ArrayList<>(maxVertices);
                break;
            case "vertex":
                handleVertex(tokens);
                break;

            // --- Géométrie : Formes ---
            case "sphere":
                handleSphere(tokens);
                break;
            case "plane":
                handlePlane(tokens);
                break;
            case "tri":
                handleTriangle(tokens);
                break;

            default:
                System.err.println("Avertissement : Commande inconnue '" + command + "' ignorée.");
        }
    }

    // =============================================================================================
    //                                     HANDLERS SPÉCIFIQUES
    // =============================================================================================

    private void handleCamera(String[] tokens) {
        Point lookFrom = parsePoint(tokens, 1);
        Point lookAt = parsePoint(tokens, 4);
        Vector up = new Vector(
            Double.parseDouble(tokens[7]),
            Double.parseDouble(tokens[8]),
            Double.parseDouble(tokens[9])
        );
        double fov = Double.parseDouble(tokens[10]);
        
        scene.setCamera(new Camera(lookFrom, lookAt, up, fov));
    }

    private void handleDirectionalLight(String[] tokens) {
        Vector direction = new Vector(
            Double.parseDouble(tokens[1]),
            Double.parseDouble(tokens[2]),
            Double.parseDouble(tokens[3])
        );
        Color color = parseColor(tokens, 4);
        
        scene.addLight(new DirectionalLight(direction, color));
    }

    private void handlePointLight(String[] tokens) {
        Point position = parsePoint(tokens, 1);
        Color color = parseColor(tokens, 4);
        
        scene.addLight(new PointLight(position, color));
    }

    private void handleVertex(String[] tokens) {
        if (vertices.size() >= maxVertices) {
            throw new IllegalStateException("Dépassement du nombre maximum de vertex déclaré (" + maxVertices + ").");
        }
        vertices.add(parsePoint(tokens, 1));
    }

    private void handleSphere(String[] tokens) {
        Point center = parsePoint(tokens, 1);
        double radius = Double.parseDouble(tokens[4]);
        
        scene.addShape(new Sphere(center, radius, currentDiffuse, currentSpecular, currentShininess));
    }

    private void handlePlane(String[] tokens) {
        Point point = parsePoint(tokens, 1);
        Vector normal = new Vector(
            Double.parseDouble(tokens[4]),
            Double.parseDouble(tokens[5]),
            Double.parseDouble(tokens[6])
        );
        
        scene.addShape(new Plane(point, normal, currentDiffuse, currentSpecular, currentShininess));
    }

    private void handleTriangle(String[] tokens) {
        int indexA = Integer.parseInt(tokens[1]);
        int indexB = Integer.parseInt(tokens[2]);
        int indexC = Integer.parseInt(tokens[3]);

        if (indexA >= maxVertices || indexB >= maxVertices || indexC >= maxVertices) {
            throw new IllegalArgumentException("Index de vertex hors limites (maxverts=" + maxVertices + ").");
        }

        Point a = vertices.get(indexA);
        Point b = vertices.get(indexB);
        Point c = vertices.get(indexC);

        scene.addShape(new Triangle(a, b, c, currentDiffuse, currentSpecular, currentShininess));
    }

    // =============================================================================================
    //                                     UTILITAIRES DE PARSING
    // =============================================================================================

    /**
     * Parse une couleur à partir de 3 tokens consécutifs (R G B).
     */
    private Color parseColor(String[] tokens, int startIndex) {
        double r = Double.parseDouble(tokens[startIndex]);
        double g = Double.parseDouble(tokens[startIndex + 1]);
        double b = Double.parseDouble(tokens[startIndex + 2]);
        
        // Note : On autorise les valeurs > 1.0 pour le HDR, 
        // mais on pourrait ajouter un avertissement ici si strict.
        return new Color(r, g, b);
    }

    /**
     * Parse un point à partir de 3 tokens consécutifs (X Y Z).
     */
    private Point parsePoint(String[] tokens, int startIndex) {
        double x = Double.parseDouble(tokens[startIndex]);
        double y = Double.parseDouble(tokens[startIndex + 1]);
        double z = Double.parseDouble(tokens[startIndex + 2]);
        return new Point(x, y, z);
    }
}