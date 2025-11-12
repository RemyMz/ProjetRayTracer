/**
 * Représente une source de lumière directionnelle (type "directional").
 * Elle implémente l'interface Light.
 * (Jalon 2)
 */
package fr.imt.raytracer.scene;

import fr.imt.raytracer.geometry.Vector;
import fr.imt.raytracer.imaging.Color;

public class DirectionalLight implements Light {

    private final Vector direction;
    private final Color color; // Dans les fichiers, c'est la couleur/intensité

    /**
     * Constructeur pour une lumière directionnelle.
     * @param direction Le vecteur de direction (x, y, z)
     * @param color La couleur/intensité (r, g, b)
     */
    public DirectionalLight(Vector direction, Color color) {
        // On normalise la direction pour s'assurer qu'elle a une longueur de 1
        this.direction = direction.normalize();
        this.color = color;
    }

    // --- Getters ---

    public Vector getDirection() {
        return direction;
    }

    public Color getColor() {
        return color;
    }
}