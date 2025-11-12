/**
 * Classe abstraite parente pour toutes les formes géométriques.
 * Stocke les propriétés de matériaux (couleurs) qui sont définies
 * avant la forme dans le fichier de scène.
 * (Basé sur Jalon 2 - Aide 4 et Jalon 5)
 */
package fr.imt.raytracer.geometry;

import fr.imt.raytracer.imaging.Color;

public abstract class Shape {

    // Propriétés de matériau (Jalon 2 & Jalon 5)
    protected Color diffuse;
    protected Color specular;
    protected double shininess; // Défini dans le Jalon 5, mais utilisé par le parser

    /**
     * Constructeur pour une forme.
     * @param diffuse Couleur diffuse actuelle
     * @param specular Couleur spéculaire actuelle
     * @param shininess Brillance actuelle (de Jalon 5)
     */
    public Shape(Color diffuse, Color specular, double shininess) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

    // --- Getters pour les matériaux ---
    // (Ces getters seront utiles plus tard, notamment au Jalon 4)
    
    public Color getDiffuse() {
        return diffuse;
    }

    public Color getSpecular() {
        return specular;
    }
    
    public double getShininess() {
        return shininess;
    }
}