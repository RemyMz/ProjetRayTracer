package fr.imt.raytracer.geometry;

import fr.imt.raytracer.imaging.Color;
import java.util.Optional;

/**
 * Représente un plan infini dans l'espace 3D.
 * <p>
 * Défini mathématiquement par un point d'ancrage $q$ et une normale $\vec{n}$.
 * Tout point $p$ du plan satisfait l'équation : $(p - q) \cdot \vec{n} = 0$.
 */
public class Plane extends Shape {

    private final Point point;   // Le point q
    private final Vector normal; // Le vecteur n

    /**
     * Construit un nouveau plan.
     *
     * @param point     Un point quelconque appartenant au plan.
     * @param normal    Le vecteur normal à la surface (sera normalisé).
     * @param diffuse   La couleur diffuse (matériau).
     * @param specular  La couleur spéculaire (reflet).
     * @param shininess Le coefficient de brillance (Phong).
     */
    public Plane(Point point, Vector normal, Color diffuse, Color specular, double shininess) {
        super(diffuse, specular, shininess);
        this.point = point;
        this.normal = normal.norm(); // Normalisation critique pour les calculs d'angles
    }

    /**
     * Calcule l'intersection entre un rayon et ce plan.
     * <p>
     * Résout l'équation paramétrique $t = \frac{(q - o) \cdot \vec{n}}{\vec{d} \cdot \vec{n}}$.
     *
     * @param ray Le rayon lancé (origine $o$, direction $\vec{d}$).
     * @return Un Optional contenant l'intersection si $t > \epsilon$, vide sinon.
     */
    @Override
    public Optional<Intersection> findIntersection(Ray ray) {
        Vector rayDirection = ray.getDirection();
        
        // Calcul du dénominateur : d . n
        double denominator = rayDirection.dot(normal);

        // 1. Vérification du parallélisme
        // Si d . n est proche de 0, le rayon est perpendiculaire à la normale, donc parallèle au plan.
        if (Math.abs(denominator) < AbstractVec3.EPSILON) {
            return Optional.empty();
        }

        // Calcul du numérateur : (q - o) . n
        // Vecteur allant de l'origine du rayon au point du plan
        Vector originToPlane = point.sub(ray.getOrigin());
        double numerator = originToPlane.dot(normal);

        // Calcul de la distance t
        double t = numerator / denominator;

        // 2. Vérification de la visibilité
        // Si t < EPSILON, l'intersection est derrière l'origine du rayon ou trop proche (auto-intersection)
        if (t < AbstractVec3.EPSILON) {
            return Optional.empty();
        }

        // Calcul du point d'impact exact : p = o + d * t
        Point intersectionPoint = ray.getPointAt(t);

        return Optional.of(new Intersection(t, intersectionPoint, normal, this));
    }

    /**
     * Retourne la normale au plan.
     * <p>
     * Pour un plan infini plat, la normale est constante quel que soit le point d'impact.
     *
     * @param p Le point d'intersection (inutilisé ici).
     * @return Le vecteur normal unitaire du plan.
     */
    @Override
    public Vector getNormalAt(Point p) {
        return normal;
    }
}