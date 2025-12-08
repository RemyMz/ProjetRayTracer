package fr.imt.raytracer.scene;

import fr.imt.raytracer.geometry.Shape;
import fr.imt.raytracer.imaging.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente la scène 3D complète à rendre.
 * <p>
 * Cette classe agit comme un conteneur centralisant tous les éléments nécessaires au rendu :
 * <ul>
 * <li>La configuration de l'image (dimensions, fichier de sortie).</li>
 * <li>L'observateur ({@link Camera}).</li>
 * <li>L'éclairage (Lumière ambiante et sources ponctuelles/directionnelles).</li>
 * <li>La géométrie (Liste des {@link Shape}).</li>
 * <li>Les paramètres de rendu globaux (ex: profondeur de récursion).</li>
 * </ul>
 */
public class Scene {

    // --- Valeurs par défaut ---
    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 480;
    private static final String DEFAULT_OUTPUT = "output.png";
    private static final Color DEFAULT_AMBIENT = new Color(0.1, 0.1, 0.1);
    private static final int DEFAULT_MAX_DEPTH = 1;

    // --- Configuration de l'image ---
    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;
    private String outputFilename = DEFAULT_OUTPUT;

    // --- Éléments de la scène ---
    private Camera camera;
    private Color ambient = DEFAULT_AMBIENT;
    
    private final List<Light> lights = new ArrayList<>();
    private final List<Shape> shapes = new ArrayList<>();

    // --- Paramètres de rendu ---
    /** Profondeur maximale pour la récursion des rayons (Réflexion). 1 = Pas de réflexion. */
    private int maxDepth = DEFAULT_MAX_DEPTH;

    /**
     * Constructeur par défaut initialisant une scène vide.
     */
    public Scene() {
        // Initialisation implicite
    }

    // =============================================================================================
    //                                     GESTION DES ÉLÉMENTS
    // =============================================================================================

    /**
     * Ajoute une source de lumière à la scène.
     * <p>
     * Vérifie la contrainte d'intensité globale : la somme des composantes de toutes les lumières
     * ne doit pas dépasser 1.0 pour éviter la saturation immédiate (Spécification Jalon 2).
     *
     * @param light La nouvelle source de lumière.
     * @throws IllegalArgumentException Si l'ajout de cette lumière entraîne une surexposition (> 1.0).
     */
    public void addLight(Light light) {
        // Calcul de l'intensité cumulée actuelle
        double totalR = light.getColor().getR();
        double totalG = light.getColor().getG();
        double totalB = light.getColor().getB();

        for (Light existingLight : lights) {
            totalR += existingLight.getColor().getR();
            totalG += existingLight.getColor().getG();
            totalB += existingLight.getColor().getB();
        }

        // Vérification des seuils (Tolérance légère pour les erreurs d'arrondi)
        if (totalR > 1.001 || totalG > 1.001 || totalB > 1.001) {
            throw new IllegalArgumentException(
                "L'intensité cumulée des lumières dépasse 1.0 ! Ajout rejeté pour : " + light.getClass().getSimpleName()
            );
        }

        this.lights.add(light);
    }

    /**
     * Ajoute un objet géométrique à la scène.
     * @param shape La forme (Sphère, Plan, Triangle) à ajouter.
     */
    public void addShape(Shape shape) {
        this.shapes.add(shape);
    }

    // =============================================================================================
    //                                     ACCESSEURS (GETTERS)
    // =============================================================================================

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getOutputFilename() { return outputFilename; }
    public Camera getCamera() { return camera; }
    public Color getAmbient() { return ambient; }
    public List<Light> getLights() { return lights; }
    public List<Shape> getShapes() { return shapes; }
    public int getMaxDepth() { return maxDepth; }

    // =============================================================================================
    //                                     MUTATEURS (SETTERS)
    // =============================================================================================

    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public void setOutputFilename(String outputFilename) { this.outputFilename = outputFilename; }
    public void setCamera(Camera camera) { this.camera = camera; }
    public void setAmbient(Color ambient) { this.ambient = ambient; }
    public void setMaxDepth(int maxDepth) { this.maxDepth = maxDepth; }
}