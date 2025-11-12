/**
 * Représente une Sphère 3D.
 * C'est la première forme géométrique que nous gérons.
 * (Jalon 2 & Jalon 3)
 */
package fr.imt.raytracer.geometry;

import fr.imt.raytracer.imaging.Color;

public class Sphere extends Shape {

    private final Point center;
    private final double radius;

    /**
     * Constructeur pour une Sphère.
     *
     * @param center Le point central (x, y, z) de la sphère.
     * @param radius Le rayon (r) de la sphère.
     * @param diffuse La couleur diffuse à appliquer à cette sphère.
     * @param specular La couleur spéculaire à appliquer à cette sphère.
     * @param shininess La brillance à appliquer à cette sphère.
     */
    public Sphere(Point center, double radius, Color diffuse, Color specular, double shininess) {
        // Appelle le constructeur de la classe parente 'Shape'
        // pour stocker les propriétés de matériau.
        super(diffuse, specular, shininess);
        
        this.center = center;
        this.radius = radius;
    }

    // --- Getters ---

    public Point getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }
}