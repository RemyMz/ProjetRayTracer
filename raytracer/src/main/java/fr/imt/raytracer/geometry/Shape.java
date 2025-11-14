package fr.imt.raytracer.geometry;

import fr.imt.raytracer.imaging.Color;
// Importer les nouvelles classes
import java.util.Optional;

/**
 * Classe parente abstraite pour tous les objets 3D de la scène.
 * (Mise à jour Jalon 3)
 */
public abstract class Shape {

    // Les matériaux (Jalon 2)
    protected final Color diffuse;
    protected final Color specular;
    protected final double shininess; // (Pour Jalon 5)

    /**
     * Constructeur pour une forme.
     * @param diffuse Couleur de la réflexion diffuse
     * @param specular Couleur de la réflexion spéculaire
     * @param shininess Exposant de brillance (pour Phong)
     */
    public Shape(Color diffuse, Color specular, double shininess) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }

    // --- Getters pour les matériaux ---
    // (Le Jalon 4 en aura besoin)
    
    public Color getDiffuse() {
        return diffuse;
    }

    public Color getSpecular() {
        return specular;
    }

    public double getShininess() {
        return shininess;
    }
    
    // --- NOUVELLES MÉTHODES ABSTRAITES (Jalon 3 & 4) ---

    /**
     * Calcule si un rayon donné intersecte cette forme.
     * (Jalon 3)
     *
     * @param ray Le rayon à tester.
     * @return Un Optional contenant l'Intersection si elle existe,
     * sinon Optional.empty().
     */
    public abstract Optional<Intersection> findIntersection(Ray ray);

    /**
     * Calcule le vecteur normal à la surface de la forme au point donné.
     * (Jalon 4)
     *
     * @param point Le point sur la surface de la forme.
     * @return Le vecteur normal (normalisé) à ce point.
     */
    public abstract Vector getNormalAt(Point point);
}