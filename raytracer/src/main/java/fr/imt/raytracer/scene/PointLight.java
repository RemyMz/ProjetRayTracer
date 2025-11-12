/**
 * Représente une source de lumière ponctuelle (type "point").
 * Elle implémente l'interface Light.
 * (Jalon 2)
 */
package fr.imt.raytracer.scene;

import fr.imt.raytracer.geometry.Point;
import fr.imt.raytracer.imaging.Color;

public class PointLight implements Light {

    private final Point position;
    private final Color color; // Dans les fichiers, c'est la couleur/intensité

    /**
     * Constructeur pour une lumière ponctuelle.
     * @param position Le point d'origine (x, y, z)
     * @param color La couleur/intensité (r, g, b)
     */
    public PointLight(Point position, Color color) {
        this.position = position;
        this.color = color;
    }

    // --- Getters ---
    
    public Point getPosition() {
        return position;
    }

    public Color getColor() {
        return color;
    }
}