package fr.imt.raytracer.scene;

import fr.imt.raytracer.geometry.Point;
import fr.imt.raytracer.geometry.Vector;
import fr.imt.raytracer.imaging.Color;

/**
 * Une source de lumière ponctuelle.
 * (Mise à jour Jalon 4)
 */
public class PointLight implements Light {

    private final Point position;
    private final Color color;

    public PointLight(Point position, Color color) {
        this.position = position;
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
     * Pour une lumière ponctuelle, la direction est le vecteur
     * allant du point d'intersection vers la position de la lumière.
     * Formule: (position_lumière - point_intersection).normalize()
     */
    @Override
    public Vector getDirectionFrom(Point p) {
        return this.position.subtract(p).normalize();
    }
    
    public Point getPosition() {
        return position;
    }
}