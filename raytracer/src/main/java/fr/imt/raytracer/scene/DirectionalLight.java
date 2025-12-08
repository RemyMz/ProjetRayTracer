package fr.imt.raytracer.scene;

import fr.imt.raytracer.geometry.Point;
import fr.imt.raytracer.geometry.Vector;
import fr.imt.raytracer.imaging.Color;

/**
 * Représente une source de lumière directionnelle (ou lumière globale).
 * <p>
 * Ce type de lumière est considéré comme étant infiniment éloigné (ex: le Soleil).
 * Par conséquent, les rayons lumineux qui en émanent sont tous parallèles entre eux.
 * Contrairement à une lumière ponctuelle, elle n'a pas de position et ne subit pas d'atténuation avec la distance.
 */
public class DirectionalLight extends Light {

    /**
     * Le vecteur direction constant pointant DE la surface VERS la source de lumière (vecteur L).
     */
    private final Vector directionToLight;

    /**
     * Construit une nouvelle lumière directionnelle.
     *
     * @param propagationDirection La direction de propagation des rayons (telle que définie dans le fichier de scène).
     * @param color                La couleur (intensité) de la lumière.
     */
    public DirectionalLight(Vector propagationDirection, Color color) {
        super(color);
        
        // 1. On normalise pour avoir une direction pure.
        // 2. On inverse le vecteur (mult -1).
        //    POURQUOI ? Le fichier de scène définit le sens des photons (ex: du ciel vers le sol).
        //    Mais le modèle de Lambert (N . L) requiert le vecteur L allant du point d'impact VERS la lumière.
        this.directionToLight = propagationDirection.norm().mult(-1.0);
    }

    /**
     * Retourne la direction vers la source de lumière depuis un point donné.
     * <p>
     * Pour une source directionnelle, ce vecteur est constant et indépendant de la position du point $P$.
     *
     * @param p Le point d'origine sur la surface (inutilisé ici).
     * @return Le vecteur unitaire $L$ pointant vers la lumière.
     */
    @Override
    public Vector getDirectionFrom(Point p) {
        return directionToLight;
    }
}