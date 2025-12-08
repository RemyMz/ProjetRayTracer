package fr.imt.raytracer.geometry;

import fr.imt.raytracer.imaging.Color;
import java.util.Optional;

/**
 * Classe de base abstraite représentant un objet géométrique de la scène.
 * <p>
 * Une forme (Shape) définit à la fois sa géométrie (via les méthodes abstraites)
 * et les propriétés matérielles de sa surface (couleurs diffuse/spéculaire, brillance)
 * utilisées pour le calcul de l'éclairage.
 */
public abstract class Shape {

    /** La composante diffuse de la couleur (aspect mat). */
    protected Color diffuse;

    /** La composante spéculaire de la couleur (reflets brillants). */
    protected Color specular;

    /** Le coefficient de brillance (exposant de Phong). Plus il est élevé, plus le reflet est petit et net. */
    protected double shininess;

    /**
     * Construit une nouvelle forme avec des propriétés matérielles spécifiques.
     *
     * @param diffuse   La couleur diffuse de l'objet.
     * @param specular  La couleur spéculaire de l'objet.
     * @param shininess Le facteur de brillance (généralement > 0).
     */
    public Shape(Color diffuse, Color specular, double shininess) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

    /**
     * Calcule l'intersection entre un rayon et cette forme.
     * <p>
     * Les classes concrètes doivent résoudre les équations algébriques ou géométriques
     * spécifiques à leur forme (ex: quadratique pour les sphères, système linéaire pour les triangles).
     *
     * @param ray Le rayon de vue ou d'ombre.
     * @return Un {@link Optional} contenant les détails de l'{@link Intersection} si elle existe,
     * vide sinon.
     */
    public abstract Optional<Intersection> findIntersection(Ray ray);

    /**
     * Calcule le vecteur normal à la surface en un point donné.
     * <p>
     * Le vecteur retourné doit être normalisé (longueur = 1).
     *
     * @param p Le point sur la surface où la normale est requise.
     * @return Le vecteur normal unitaire au point p.
     */
    public abstract Vector getNormalAt(Point p);

    // --- Accesseurs (Getters) ---

    public Color getDiffuse() {
        return diffuse;
    }

    public Color getSpecular() {
        return specular;
    }

    public double getShininess() {
        return shininess;
    }

    // --- Mutateurs (Setters) ---
    // Utilisés principalement par le SceneFileParser pour appliquer l'état courant des matériaux.

    public void setDiffuse(Color diffuse) {
        this.diffuse = diffuse;
    }

    public void setSpecular(Color specular) {
        this.specular = specular;
    }

    public void setShininess(double shininess) {
        this.shininess = shininess;
    }
}