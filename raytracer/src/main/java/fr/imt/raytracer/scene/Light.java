package fr.imt.raytracer.scene;

import fr.imt.raytracer.geometry.Point;
import fr.imt.raytracer.geometry.Vector;
import fr.imt.raytracer.imaging.Color;

/**
 * Abstraction représentant une source de lumière dans la scène.
 * <p>
 * Une lumière est définie minimalement par sa couleur (intensité spectrale).
 * Les sous-classes (Ponctuelle, Directionnelle) doivent définir la géométrie de l'émission,
 * c'est-à-dire comment calculer le vecteur d'incidence sur une surface.
 */
public abstract class Light {
    
    /** La couleur et l'intensité de la lumière émise. */
    private final Color color;

    /**
     * Construit une nouvelle source de lumière.
     *
     * @param color La couleur (intensité RGB) de la lumière.
     */
    public Light(Color color) {
        this.color = color;
    }

    /**
     * Retourne la couleur de la lumière.
     * @return L'objet Color associé.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Calcule le vecteur direction allant d'un point de la surface vers cette source de lumière.
     * <p>
     * Ce vecteur (souvent noté $L$ dans les équations) est fondamental pour calculer :
     * <ul>
     * <li>L'angle d'incidence (Loi de Lambert : $N \cdot L$).</li>
     * <li>Les vecteurs de réflexion spéculaire (Modèle de Phong).</li>
     * </ul>
     *
     * @param p Le point d'intersection sur la surface de l'objet.
     * @return Le vecteur unitaire pointant de $P$ vers la source lumineuse.
     */
    public abstract Vector getDirectionFrom(Point p);
}