package fr.imt.raytracer.scene;

import fr.imt.raytracer.geometry.Point;
import fr.imt.raytracer.geometry.Vector;
import fr.imt.raytracer.imaging.Color;

/**
 * Une source de lumière directionnelle (ex: soleil).
 * (Mise à jour Jalon 4)
 */
public class DirectionalLight implements Light {

    private final Vector direction;
    private final Color color;

    public DirectionalLight(Vector direction, Color color) {
        // On normalise la direction au cas où
        this.direction = direction.normalize(); 
        this.color = color;
    }

    /**
     * Retourne la couleur de la lumière.
     */
    @Override
    public Color getColor() {
        return this.color;
    }

    /**
     * Pour une lumière directionnelle, la direction est constante
     * (elle vient de l'infini).
     * On retourne l'opposé de la direction du fichier scène,
     * car le fichier scène indique D'OÙ la lumière vient
     * (ex: 0,0,-1 = vient de Z+ vers Z-).
     * Nous, on veut la direction VERS la lumière.
     * Formule: -direction_fichier_scene
     */
    @Override
    public Vector getDirectionFrom(Point p) {
        // On ignore 'p' car la lumière est directionnelle (infinie)
        // On retourne l'inverse de la direction (vecteur * -1)
        return this.direction.multiply(-1.0);
    }

    // Getter spécifique à la lumière directionnelle (optionnel pour l'interface Light mais utile)
    public Vector getDirection() {
        return direction;
    }
}