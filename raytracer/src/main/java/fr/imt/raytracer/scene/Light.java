package fr.imt.raytracer.scene;

import fr.imt.raytracer.geometry.Point;
import fr.imt.raytracer.geometry.Vector;
import fr.imt.raytracer.imaging.Color;

/**
 * Interface pour tous les objets émettant de la lumière.
 * (Mise à jour Jalon 4)
 */
public interface Light {
    
    /**
     * Retourne la couleur (intensité) de la lumière.
     * @return La couleur de la lumière.
     */
    Color getColor();

    /**
     * Calcule le vecteur direction de la lumière vers un point donné.
     * @param p Le point sur la surface de l'objet.
     * @return Le vecteur direction (normalisé) DEPUIS le point VERS la lumière.
     */
    Vector getDirectionFrom(Point p);

}