/**
 * Classe principale qui contient tous les éléments d'une scène 3D.
 * C'est cette classe que le Parser va remplir.
 * (Basé sur Jalon 2 - Aide 4)
 */
package fr.imt.raytracer.scene;

import java.util.ArrayList;
import java.util.List;

import fr.imt.raytracer.geometry.Point;
import fr.imt.raytracer.geometry.Shape;
import fr.imt.raytracer.imaging.Color;

public class Scene {

    // Dimensions de l'image (de "size")
    private int width;
    private int height;

    // Nom du fichier de sortie (de "output")
    private String outputFilename = "output.png"; // Valeur par défaut (Jalon 2)

    // Caméra (de "camera")
    private Camera camera;

    // Couleur ambiante (de "ambient")
    private Color ambientLight = new Color(0, 0, 0); // Par défaut

    // Liste des lumières (de "directional" et "point")
    private List<Light> lights = new ArrayList<>();

    // Liste des formes (de "sphere", "tri", "plane")
    private List<Shape> shapes = new ArrayList<>();

    // Liste des points/vertex (de "vertex" pour les triangles)
    private List<Point> vertices = new ArrayList<>();

    // Constructeur
    public Scene() {
        // Le constructeur est vide pour l'instant,
        // le SceneFileParser remplira les champs.
    }

    // --- Getters et Setters ---
    // Le parser aura besoin de ces méthodes pour remplir la scène.

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public void setOutputFilename(String outputFilename) {
        this.outputFilename = outputFilename;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Color getAmbientLight() {
        return ambientLight;
    }

    public void setAmbientLight(Color ambientLight) {
        this.ambientLight = ambientLight;
    }

    public List<Light> getLights() {
        return lights;
    }

    public void addLight(Light light) {
        this.lights.add(light);
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void addShape(Shape shape) {
        this.shapes.add(shape);
    }

    public List<Point> getVertices() {
        return vertices;
    }

    public void addVertex(Point vertex) {
        this.vertices.add(vertex);
    }
}