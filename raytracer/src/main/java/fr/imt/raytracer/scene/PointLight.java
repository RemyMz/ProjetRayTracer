package fr.imt.raytracer.scene;

import fr.imt.raytracer.geometry.Point;
import fr.imt.raytracer.geometry.Vector;
import fr.imt.raytracer.imaging.Color;

/**
 * Représente une source de lumière ponctuelle (Point Light).
 * <p>
 * Une lumière ponctuelle est définie par une position précise dans l'espace.
 * Elle émet de la lumière de manière omnidirectionnelle (dans toutes les directions)
 * depuis ce point.
 * <p>
 * Exemples physiques : Une ampoule, une bougie, une LED.
 */
public class PointLight extends Light {

    /** La position spatiale de la source lumineuse. */
    private final Point position;

    /**
     * Construit une nouvelle lumière ponctuelle.
     *
     * @param position La position de la lumière dans la scène.
     * @param color    La couleur (et l'intensité) de la lumière émise.
     */
    public PointLight(Point position, Color color) {
        super(color);
        this.position = position;
    }

    /**
     * Retourne la position de la source de lumière.
     * @return Le point d'origine de l'émission.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Calcule la direction vers la source de lumière depuis un point donné.
     * <p>
     * Contrairement à une lumière directionnelle, ce vecteur dépend de la position
     * relative du point $P$ sur la surface.
     *
     * @param p Le point d'origine sur la surface de l'objet.
     * @return Le vecteur unitaire $\vec{L}$ allant de $P$ vers la lumière ($\text{Pos} - P$).
     */
    @Override
    public Vector getDirectionFrom(Point p) {
        // Calcul du vecteur : Destination (Lumière) - Origine (Surface)
        // La normalisation est essentielle pour le produit scalaire (Lambert)
        return position.sub(p).norm();
    }
}